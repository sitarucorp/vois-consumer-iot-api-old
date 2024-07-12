package com.vois.consumer.iot.events.specs;

import lombok.Getter;

@Getter
public enum ResponseDescriptionMessageEnum {
    DATA_REFRESHED("data refreshed"),

    ERROR_NO_DATA_FOUND("ERROR: no data file found"),

    ERROR_TECHNICAL_EXCEPTION_OCCURED("ERROR: A technical exception occurred"),

    SUCCESS_LOCATION_IDENTIFIED("SUCCESS: Location identified."),

    SUCCESS_BUT_AIRPLANE_MODE_IS_ON("SUCCESS: Location not available: Please turn off airplane mode."),

    ERROR_DEVICE_COULD_NOT_LOCATED("ERROR: Device could not be located"),

    ERROR_PRODUCT_ID_NOT_FOUND("ERROR: Id {productId} not found");


    public static String replaceResourceValueGetDescription(ResponseDescriptionMessageEnum val , String resource) {
        return val.description.replace("{productId}" , resource);
    }

    private final String description;

    ResponseDescriptionMessageEnum(String description) {
        this.description = description;
    }

}
