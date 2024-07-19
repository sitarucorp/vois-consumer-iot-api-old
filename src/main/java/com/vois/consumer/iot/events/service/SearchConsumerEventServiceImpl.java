package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import com.vois.consumer.iot.events.dto.ConsumerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class SearchConsumerEventServiceImpl implements SearchConsumerEventService {

    private final EventDataCarrier eventDataCarrier;

    public SearchConsumerEventServiceImpl(EventDataCarrier eventDataCarrier) {
        this.eventDataCarrier = eventDataCarrier;
    }

    @Override
    public Optional<ConsumerEvent> searchProductEvent(String productId , Optional<String> timestamp) {
        try {
            return timestamp.map(s -> eventDataCarrier.lookup(productId , Long.parseLong(s)))
                    .or(() ->
                            Optional.ofNullable(eventDataCarrier.lookup(productId , Instant.now().toEpochMilli())));
        } catch (NumberFormatException e) {
            log.error("SearchConsumerEventServiceImpl : NumberFormatException : timestamp = '{}', exception={}" , timestamp ,
                    e.getMessage());
            return Optional.empty();
        }
    }
}
