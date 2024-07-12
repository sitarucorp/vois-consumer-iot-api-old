package com.vois.consumer.iot.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Data
public class ErrorResponse implements BasicResponse {
    private String description;

    public ErrorResponse(String description) {
        this.description = description;
    }
}
