package com.vois.consumer.iot.events.specs;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public enum BatteryLifeEnum {

    CRITICAL(new float[]{0.01F , 0.099F} , "Critical"),
    LOW(new float[]{0.1F , 0.399F} , "Low"),
    MEDIUM(new float[]{0.4F , 0.599F} , "Medium"),
    HIGH(new float[]{0.6F , 0.979F} , "High"),
    FULL(new float[]{0.98F , 1.0F} , "Full"),
    UNKNOWN(new float[]{0.0F , 0.0F} , "Unknown"); // Adjusted range for UNKNOWN

    private final float[] range;
    private final String name;

    BatteryLifeEnum(float[] range , String name) {
        this.range = range;
        this.name = name;
    }

    public static String getBatteryLife(String value) {
        if(StringUtils.isBlank(value)) {
            return UNKNOWN.getName();
        }
        return getBatteryLife(Float.parseFloat(value));
    }

    public static String getBatteryLife(@NonNull float value) {
        for(BatteryLifeEnum batteryLifeEnum : values()) {
            if(isInRange(value , batteryLifeEnum.range[0] , batteryLifeEnum.range[1])) {
                return batteryLifeEnum.getName();
            }
        }
        return UNKNOWN.getName();
    }

    private static boolean isInRange(float value , float min , float max) {
        return value >= min && value <= max;
    }
}
