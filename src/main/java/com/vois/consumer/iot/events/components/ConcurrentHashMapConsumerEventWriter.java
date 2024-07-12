package com.vois.consumer.iot.events.components;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.dto.EventDataCarrier;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Data
public class ConcurrentHashMapConsumerEventWriter implements ItemWriter<ConsumerEvent> {
    private EventDataCarrier eventDataCarrier;

    @Override
    @SuppressWarnings("unchecked")
    public void write(Chunk<? extends ConsumerEvent> chunk) throws Exception {
        eventDataCarrier.addAll((List<ConsumerEvent>) chunk.getItems());
    }
}
