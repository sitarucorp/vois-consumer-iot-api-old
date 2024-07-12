package com.vois.consumer.iot.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ConsumerEvent {
    private String datetime;
    private String eventId;
    private String productId;
    private String latitude;
    private String longitude;
    private String battery;
    private String light;
    private String airplaneMode;
}
