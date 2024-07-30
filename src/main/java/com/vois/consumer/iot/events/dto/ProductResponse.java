package com.vois.consumer.iot.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vois.consumer.iot.events.specs.AirplaneModeEnum;
import com.vois.consumer.iot.events.specs.BatteryLifeEnum;
import com.vois.consumer.iot.events.specs.ProductIdNameEnum;
import com.vois.consumer.iot.events.util.IotEventUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductResponse implements BasicResponse {
    private String id;
    private String name;
    private String datetime;
    @JsonProperty("long")
    private String longitude;
    @JsonProperty("lat")
    private String latitude;
    private String status;
    private String battery;
    private String description;

    public static ProductResponse toProductResponse(@NonNull ConsumerEvent consumerEvent) {
        boolean isAirplaneModeOn = consumerEvent.getAirplaneMode().equals(AirplaneModeEnum.ON.name());
        return new ProductResponseBuilder()
                .id(consumerEvent.getProductId())
                .name(ProductIdNameEnum.getProductNameByProductId(consumerEvent.getProductId()))
                .datetime(IotEventUtil.convertMillisecondsToVoisReadableFormat(consumerEvent.getDatetime()))
                .status(IotEventUtil.getStatusByLatLong(consumerEvent.getLatitude() , consumerEvent.getLongitude() , isAirplaneModeOn))
                .battery(BatteryLifeEnum.getBatteryLife(consumerEvent.getBattery()))
                .description(IotEventUtil.getDescriptionMessageOnSuccessfullIdentificationOfEvent(consumerEvent, isAirplaneModeOn))
                .latitude(isAirplaneModeOn ? StringUtils.EMPTY : consumerEvent.getLatitude())
                .longitude(isAirplaneModeOn ? StringUtils.EMPTY : consumerEvent.getLongitude())
                // checking here on lat, long information
                .build();
    }
}
