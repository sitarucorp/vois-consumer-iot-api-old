package com.vois.consumer.iot.events.config;

import com.vois.consumer.iot.events.components.ConcurrentHashMapConsumerEventWriter;
import com.vois.consumer.iot.events.components.EventDataCarrier;
import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.dto.EventConfig;
import com.vois.consumer.iot.events.processor.ConsumerEventProcessor;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class ConsumerEventBatchConfig {

    private EventConfig eventConfig;

    @Bean
    public ConsumerEventProcessor consumerEventProcessor() {
        return new ConsumerEventProcessor();
    }

    @Bean
    public ConcurrentHashMap<String, ConsumerEvent> concurrentHashMapEvents() {
        return new ConcurrentHashMap<String, ConsumerEvent>();
    }


    @Bean
    public ConcurrentHashMapConsumerEventWriter concurrentHashMapConsumerEventWriter(EventDataCarrier eventDataCarrier) {
        return new ConcurrentHashMapConsumerEventWriter(eventDataCarrier);
    }

    @Bean
    public FlatFileItemReader<ConsumerEvent> getFlatFileItemReader() {
        return new FlatFileItemReaderBuilder<ConsumerEvent>()
                .name("voisConsumerEventCsvReader")
                .resource(new ClassPathResource(eventConfig.getDefaultDataCsvFilePath()))
                .delimited()
                .strict(false)
                .names("DateTime" , "EventId" , "ProductId" , "Latitude" , "Longitude" , "Battery" , "Light" , "AirplaneMode")
                .targetType(ConsumerEvent.class)
                .build();
    }
}
