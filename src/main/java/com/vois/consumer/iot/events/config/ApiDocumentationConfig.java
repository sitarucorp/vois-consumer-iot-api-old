package com.vois.consumer.iot.events.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocumentationConfig {

    @Value("${version:unknown}")
    private String version;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("VoIS Consumer Event Processing Application")
                .title("Consumer IoT Events Data Reports Application")
                .version(version)
                .summary("IoT Geo-Coordinates Data Processing Application")
                .description("Java Spring Boot microservice to expose a simple REST API that reports the location of IoT tracking devices"
                        +" loaded in batches from a CSV file."));
    }
}

