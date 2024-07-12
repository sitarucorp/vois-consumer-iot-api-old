package com.vois.consumer.iot.events.service;

import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DataLoadingServiceImpl implements DataLoadingService {

    @Override
    public boolean loadDataFromFile(String filePath) throws NoConsumerEventSourceDataFileFoundException {
        validateIfFileExists(filePath);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        log.info("thread.count: {}" , 4);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                log.info("{}" , line);
                final String lineToProcess = line;
                executor.submit(() -> processLine(lineToProcess));
            }
        } catch (IOException e) {
            log.error("Error occurred while reading file {}" , filePath , e);
        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(30 , TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Error while closing executor on closing file {}" , filePath , e);
            }
        }
        return true;
    }

    @Override
    public boolean validateIfFileExists(String filePath) throws NoConsumerEventSourceDataFileFoundException {
        if(Files.exists(Path.of(filePath) , LinkOption.NOFOLLOW_LINKS)) {
            return true;
        }
        throw new NoConsumerEventSourceDataFileFoundException("file does not exist: " + filePath);
    }

    private static void processLine(String line) {
        String[] values = line.split(","); // Assuming comma as delimiter
        // Process values array
        System.out.println(values[0]); // Example: print the first column
    }
}
