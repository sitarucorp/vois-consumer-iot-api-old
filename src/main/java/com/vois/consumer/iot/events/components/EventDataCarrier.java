package com.vois.consumer.iot.events.components;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.util.IotEventUtil;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
@Component
@Scope("prototype")
public class EventDataCarrier {

    private ConcurrentHashMap<String, ConsumerEvent>[] concurrentHashMaps;

    private static boolean leader = true;

    public EventDataCarrier(ConcurrentHashMap<String, ConsumerEvent>[] concurrentHashMapEvents) {
        this.concurrentHashMaps = concurrentHashMapEvents;
        leader = true;
    }

    public void add(ConsumerEvent event) {
        concurrentHashMaps[0].put(event.getEventId() , event);
    }

    public void addAll(List<ConsumerEvent> events) {
        concurrentHashMaps[0].putAll(events.stream().collect(
                Collectors.toConcurrentMap(ConsumerEvent::getEventId , event -> event)));
        leader = true;
    }

    public void reset() {
        leader = false;
        concurrentHashMaps[1] = new ConcurrentHashMap<>(concurrentHashMaps[0]);         // copy
        concurrentHashMaps[0].clear();  // leaning leader
    }

    public ConsumerEvent lookup(String productId , long timestamp) {
        int index = leader ? 0 : 1;
        LinkedHashMap<String, String> filteredEvents = concurrentHashMaps[index].values().stream()
                .filter(event -> event.getProductId().equals(productId))
                .sorted(Comparator.comparing(ConsumerEvent::getDatetime))
                .collect(Collectors.toMap(ConsumerEvent::getEventId , ConsumerEvent::getDatetime , (e , r) -> e , LinkedHashMap::new));

        Optional<String> eventId = IotEventUtil.findNearestTimestampEventId(filteredEvents , timestamp);
        return eventId.map(event -> concurrentHashMaps[index].get(event)).orElse(null);
    }

    public int getNumberOfRecordsInMemory() {
        return concurrentHashMaps[leader ? 0 : 1].size();
    }
}