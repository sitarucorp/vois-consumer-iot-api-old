package com.vois.consumer.iot.events.components;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.util.IotEventUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EventDataCarrier {

    private ConcurrentHashMap<String, ConsumerEvent>[] concurrentHashMaps;

    private static boolean leader = true;

    public EventDataCarrier(ConcurrentHashMap<String, ConsumerEvent> leaderDataMap , ConcurrentHashMap<String, ConsumerEvent> memberDataMap) {
        this.concurrentHashMaps = new ConcurrentHashMap[2];
        this.concurrentHashMaps[0] = leaderDataMap;
        this.concurrentHashMaps[1] = memberDataMap;
        leader = true;
    }

    public void add(ConsumerEvent event) {
        concurrentHashMaps[0].put(event.getEventId() , event);
        leader = true;
    }

    public void addAll(List<ConsumerEvent> events) {
        concurrentHashMaps[0].putAll(events.stream().collect(
                Collectors.toConcurrentMap(ConsumerEvent::getEventId , event -> event)));
        leader = true;
    }

    public void reset() {
        leader = false;
        concurrentHashMaps[1] = new ConcurrentHashMap<>(concurrentHashMaps[0]);         // replacing the secondary
        log.warn("until the leader map get loaded, delegating content to member map to serve intermediate request until full data "
                + "is loaded and leader is up again");
        concurrentHashMaps[0].clear();  // leaning leader
    }

    public ConsumerEvent lookup(String productId , long timestamp) {
        int index = leader ? 0 : 1;
        LinkedHashMap<String, String> filteredEvents = concurrentHashMaps[index].values().stream()
                .filter(event -> event.getProductId().equals(productId))
                .sorted(Comparator.comparing(ConsumerEvent::getDatetime))
                .collect(Collectors.toMap(ConsumerEvent::getEventId , ConsumerEvent::getDatetime ,
                        (event , datetime) -> event , LinkedHashMap::new));

        Optional<String> eventId = IotEventUtil.findNearestTimestampEventId(filteredEvents , timestamp);
        return eventId.map(event -> concurrentHashMaps[index].get(event)).orElse(null);
    }

    public int getNumberOfRecordsInMemory() {
        return concurrentHashMaps[leader ? 0 : 1].size();
    }
}
