package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;

import java.util.Optional;

public interface DataLoadingService {
    /**
     * This initially loads data for the API at the start of application
     */
    Integer loadDataFromFile(String filePath) throws NoConsumerEventSourceDataFileFoundException;

    //boolean reset();

    boolean validateIfFileExists(String filePath) throws NoConsumerEventSourceDataFileFoundException;
    /**
     * This gets called every time the API gets hit, so each API call we are having an file I/O.
     */
    default Optional<Integer> refreshDataFromFile(String filePath) throws NoConsumerEventSourceDataFileFoundException {
        //reset();
        return Optional.of(loadDataFromFile(filePath));
    }
}
