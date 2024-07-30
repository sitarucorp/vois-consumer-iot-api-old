package com.vois.consumer.iot.events.specs;

import org.springframework.lang.NonNull;

public enum ProductIdNameEnum {

    GeneralTracker("69"),
    CyclePlusTracker("WG"),
    Unknown("");
    // if they're too many move it to the config json and load from there

    private final String productIdPrefix;

    ProductIdNameEnum(String productIdPrefix) {
        this.productIdPrefix = productIdPrefix;
    }

    public static String getProductNameByProductId(@NonNull String productId) {
        for(ProductIdNameEnum productIdNameEnum : values()) {
            if(productId.startsWith(productIdNameEnum.productIdPrefix)) {
                return productIdNameEnum.name();
            }
        }
        return Unknown.name();
    }
}
