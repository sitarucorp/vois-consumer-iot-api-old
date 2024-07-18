package com.vois.consumer.iot.events;

import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import com.vois.consumer.iot.events.service.DataLoadingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@SpringBootApplication
@Slf4j
public class VoisConsumerIoTEventsApplication {

    private DataLoadingServiceImpl dataLoadingService;

    @Value("${version:unknown}")
    private String version;

    @Value("${vois.consumer.events.data.csvfile.path:/app/data.csv}")
    private String defaultFilePath;

    public VoisConsumerIoTEventsApplication(DataLoadingServiceImpl dataLoadingService) {
        this.dataLoadingService = dataLoadingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(VoisConsumerIoTEventsApplication.class , args);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("VoisConsumerIoTEventsApplication context refreshed with version : {}" , version);
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping" , RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
                .getHandlerMethods();
        map.forEach((key , value) -> log.info("VoisConsumerIoTEventsApplication '{}' : {} {}" , version , key , value));
    }

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) throws NoConsumerEventSourceDataFileFoundException {
        log.info("VoisConsumerIoTEventsApplication application started");
        if("true".equals(System.getenv().getOrDefault("load_events_available" , "true"))) {
            log.info("is file available : "+dataLoadingService.validateIfFileExists(defaultFilePath));
            dataLoadingService.loadDataFromFile(defaultFilePath);
        } else {
            log.info("skipping default data load as load_events_available environment variable is not set to true");
        }
    }
}
