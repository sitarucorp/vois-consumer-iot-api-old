package com.vois.consumer.iot.events.processor;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ConsumerEventProcessor implements ItemProcessor<ConsumerEvent, ConsumerEvent> {
    @Override
    public ConsumerEvent process(ConsumerEvent item) throws Exception {
        // INFO should we need to process or work on the data, we would do this here.
        return item;
    }
}
