package com.vois.consumer.iot.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class EventConfig {
    private String defaultDataCsvFilePath;
}
