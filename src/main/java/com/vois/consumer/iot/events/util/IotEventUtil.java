package com.vois.consumer.iot.events.util;

import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.specs.ResponseDescriptionMessageEnum;
import com.vois.consumer.iot.events.specs.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public final class IotEventUtil {

    private IotEventUtil() {
    }

    private static final String VOIS_DATE_FORMAT = "dd/MM/yyyy  HH:mm:ss";

    private static final String DEFAULT_ZONE_ID = "UTC";

    // FIXME girish test is pending
    public static String convertMillisecondsToVoisReadableFormat(@NonNull String epochMilliseconds) {
        if(StringUtils.isNotBlank(epochMilliseconds) && StringUtils.isNumeric(epochMilliseconds)) {
            return convertMillisecondsToVoisReadableFormat(Long.parseLong(epochMilliseconds));
        }
        return "";
    }

    public static String convertMillisecondsToVoisReadableFormat(@NonNull long epochMilliseconds) {

        if(epochMilliseconds <= 0) {  // ~ 1st Jan 1970
            return "";
        }
        try {
            return convertMillisecondsToVoisReadableFormat(Instant.ofEpochMilli(epochMilliseconds));
        } catch (DateTimeException e) {
            log.warn("Error occurred while converting milliseconds to vois format for input epoch milliseconds '{}' e={}" ,
                    epochMilliseconds , e.getMessage());
        }
        return "";
    }

    public static String convertMillisecondsToVoisReadableFormat(@NonNull Instant instant) {
        ZonedDateTime dateTime = instant.atZone(ZoneId.of(DEFAULT_ZONE_ID));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(VOIS_DATE_FORMAT);
        return dateTime.format(formatter);
    }

    public static String getStatusByLatLong(String latitude , String longitude , boolean isAirplaneModeOn) {
        return getStatusEnumByLatLong(latitude , longitude , isAirplaneModeOn).name();
    }

    private static StatusEnum getStatusEnumByLatLong(String latitude , String longitude , boolean isAirplaneModeOn) {
        if(StringUtils.isNotBlank(latitude) && StringUtils.isNotBlank(longitude) && !isAirplaneModeOn) {
            return StatusEnum.Active;
        }
        return StatusEnum.Inactive;
    }

    public static String getDescriptionMessageOnSuccessfullIdentificationOfEvent(@NonNull final ConsumerEvent consumerEvent , boolean isAirplaneModeOn) {

        if(isAirplaneModeOn) {
            return ResponseDescriptionMessageEnum.SUCCESS_BUT_AIRPLANE_MODE_IS_ON.getDescription();
        }
        StatusEnum isDeviceLocated = getStatusEnumByLatLong(
                consumerEvent.getLatitude() ,
                consumerEvent.getLongitude() , false);

        return switch (isDeviceLocated) {
            case Active -> ResponseDescriptionMessageEnum.SUCCESS_LOCATION_IDENTIFIED.getDescription();
            case Inactive, NA -> ResponseDescriptionMessageEnum.ERROR_DEVICE_COULD_NOT_LOCATED.getDescription();
        };
    }

    private static boolean isValidLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    // FIXME girish test pending
    public static Optional<String> findNearestTimestampEventId(LinkedHashMap<String, String> timestampsMap , long target) {
        LinkedList<Long> timestamps = new ArrayList<>(timestampsMap.values()).stream().filter(IotEventUtil::isValidLong)
                .map(Long::parseLong).sorted().collect(Collectors.toCollection(LinkedList::new));

        int index = Collections.binarySearch(timestamps , target);

        if(index < 0) {
            index = -index - 1;
        }

        // Determine the closest timestamp
        long nearestTimestamp;
        if(index == 0) {
            nearestTimestamp = timestamps.get(0);
        } else if(index >= timestamps.size()) {
            nearestTimestamp = timestamps.get(timestamps.size() - 1);
        } else {
            long before = timestamps.get(index - 1);
            long after = timestamps.get(index);
            if(Math.abs(target - before) <= Math.abs(target - after)) {
                nearestTimestamp = before;
            } else {
                nearestTimestamp = after;
            }
        }
        for(Map.Entry<String, String> entry : timestampsMap.entrySet()) {
            if(nearestTimestamp == Long.parseLong(entry.getValue())) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}
