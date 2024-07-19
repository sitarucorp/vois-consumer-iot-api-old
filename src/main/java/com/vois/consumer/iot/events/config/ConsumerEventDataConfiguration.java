package com.vois.consumer.iot.events.config;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import com.vois.consumer.iot.events.dto.EventConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
@Data
public class ConsumerEventDataConfiguration {

    @Value("${vois.consumer.events.data.csvfile.path}")
    private String csvFilePath;

    @Bean
    public EventConfig getEventConfig() {
        log.info("Loading consumer events with default file from configuration: '{}' " , csvFilePath);
        return new EventConfig(csvFilePath);
    }


    @Bean
    @Primary
    @Qualifier("leaderDataMap")
    public ConcurrentHashMap getLeaderDataMap(){
        return new ConcurrentHashMap();
    }

    @Bean
    @Qualifier("memberDataMap")
    public ConcurrentHashMap getMemberDataMap(){
        return new ConcurrentHashMap();
    }

    @Bean
    public EventDataCarrier getEventDataCarrier() {
        return new EventDataCarrier(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    }
}
