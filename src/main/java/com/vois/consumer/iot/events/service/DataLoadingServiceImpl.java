package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.exceptions.InvalidCsvRecordException;
import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

@Service
@Slf4j
public class DataLoadingServiceImpl implements DataLoadingService {

    private static final String COMMA = ",";
    private final EventDataCarrier eventDataCarrier;

    public DataLoadingServiceImpl(EventDataCarrier eventDataCarrier) {
        this.eventDataCarrier = eventDataCarrier;
    }

    @Override
    public final Integer loadDataFromFile(String filePath) throws NoConsumerEventSourceDataFileFoundException {
        int corruptRecordCount = 0;
        if(validateIfFileExists(filePath)) {    //case remaining && Files.lines(Path.of(filePath, Charset.defaultCharset())).count() > 1))
            log.warn("New file requested to load, resetting event data carrier path: {}" , filePath);
            eventDataCarrier.reset();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(!StringUtils.startsWith(line , "DateTime,")) {
                        log.debug("{}" , line);
                        loadDataSharedMemory(processLine(line));
                    }
                }
            } catch (IOException e) {
                throw new NoConsumerEventSourceDataFileFoundException("e=" + e.getMessage());
            } catch (InvalidCsvRecordException e) {
                ++corruptRecordCount;
                log.debug("InvalidCsvRecordException : {}, {}" , corruptRecordCount , e.getMessage());
            }
            log.info("Number of records available : " + eventDataCarrier.getNumberOfRecordsInMemory());
            return eventDataCarrier.getNumberOfRecordsInMemory()[0];
        }
        return -1;
    }


    @Override
    public boolean validateIfFileExists(String filePath) throws NoConsumerEventSourceDataFileFoundException {
        if(Files.exists(Path.of(filePath) , LinkOption.NOFOLLOW_LINKS)) {
            return true;
        }
        throw new NoConsumerEventSourceDataFileFoundException("file does not exist: " + filePath);
    }

    private void loadDataSharedMemory(ConsumerEvent event) {
        try {
            eventDataCarrier.add(event);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static ConsumerEvent processLine(String line) throws InvalidCsvRecordException {
        ConsumerEvent event = null; // Assuming comma as delimiter
        try {
            event = convert(line);
        } catch (StreamCorruptedException e) {
            log.warn("data corrupt record: size not matches 8 fields for line '{}'" , line);
            throw new InvalidCsvRecordException(e.getMessage());
        }
        log.info("event - {}" , event);
        return event;
    }

    public static ConsumerEvent convert(@NonNull String csvLine) throws StreamCorruptedException {
        String[] csvRecord = csvLine.split(COMMA);
        log.debug("record:- {}" , csvLine);

        if(csvRecord.length != 8) {
            throw new StreamCorruptedException("Invalid CSV line (fields are not exact matching 8 in length): " + csvLine);
        }

        // "DateTime" , "EventId" , "ProductId" , "Latitude" ,
        // "Longitude" , "Battery" , "Light" , "AirplaneMode")
        return ConsumerEvent.builder()
                .datetime(csvRecord[0])
                .eventId(csvRecord[1])
                .productId(csvRecord[2])
                .latitude(csvRecord[3])
                .longitude(csvRecord[4])
                .battery(csvRecord[5])
                .light(csvRecord[6])
                .airplaneMode(csvRecord[7])
                .build();
    }
}
