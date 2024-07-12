package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.components.EventDataCarrier;
import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DataLoadingServiceImpl implements DataLoadingService {

    private static final String COMMA = ",";

    private EventDataCarrier eventDataCarrier;

    public DataLoadingServiceImpl(EventDataCarrier eventDataCarrier) {
        this.eventDataCarrier = eventDataCarrier;
    }

    @Override
    public final boolean loadDataFromFile(String filePath) throws NoConsumerEventSourceDataFileFoundException {
        if(validateIfFileExists(filePath)) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            log.info("thread.count: {}" , 4);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    log.info("{}" , line);  // FIXME change to debug
                    final String lineToProcess = line;
                    executor.submit(() -> processLine(lineToProcess));
                }
            } catch (IOException e) {
                log.error("Error occurred while reading file {}" , filePath , e);
            } finally {
                executor.shutdown();
                try {
                    executor.awaitTermination(15 , TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error("Error while closing executor on closing file {}" , filePath , e);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean validateIfFileExists(String filePath) throws NoConsumerEventSourceDataFileFoundException {
        if(Files.exists(Path.of(filePath) , LinkOption.NOFOLLOW_LINKS)) {
            return true;
        }
        throw new NoConsumerEventSourceDataFileFoundException("file does not exist: " + filePath);
    }

    private static void processLine(String line) {
        ConsumerEvent event = null; // Assuming comma as delimiter
        try {
            event = convert(line);
        } catch (StreamCorruptedException e) {
            log.warn("data corrupt record: size not matches 8 fields for line '{}'" , line);
        }
        log.info("event - {}" , event);
    }

    public static ConsumerEvent convert(String csvLine) throws StreamCorruptedException {
        String[] csvRecord = csvLine.split(COMMA);
        log.debug("record:- {}" , csvLine);

        if(csvRecord.length != 8) {
            throw new StreamCorruptedException("Invalid CSV line: " + csvLine);
        }

        // "DateTime" , "EventId" , "ProductId" , "Latitude" , "Longitude" , "Battery" , "Light" , "AirplaneMode")
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
