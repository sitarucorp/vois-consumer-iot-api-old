package com.vois.consumer.iot.events.components;


import com.vois.consumer.iot.events.dto.ConsumerEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.batch.item.Chunk;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ConcurrentHashMapConsumerEventWriterTest {
    // ArgumentCaptor captor = ArgumentCaptor.forClass(EventDataCarrier.class);

    @Mock
    private EventDataCarrier eventDataCarrier;

    @Test
    public void testWriteChunksSavedWell() throws Exception {
        EventDataCarrier eventDataCarrier = new EventDataCarrier(new ConcurrentHashMap() , new ConcurrentHashMap<>());
        ConcurrentHashMapConsumerEventWriter eventWriter = new ConcurrentHashMapConsumerEventWriter(eventDataCarrier);
        eventWriter.write(new Chunk<>(
                ConsumerEvent.builder().eventId("TANGO1").build() ,
                ConsumerEvent.builder().eventId("DELTA2").build() ,
                ConsumerEvent.builder().eventId("CHARLIE3").build() ,
                ConsumerEvent.builder().eventId("MANGO4").build()
        ));
        assertEquals(4 , eventWriter.getEventDataCarrier().getNumberOfRecordsInMemory());
    }

    @Test
    public void testTestSourceAvailable() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sample.csv").getFile());
        log.info("running analysis on sample data {}",file.getAbsolutePath());

        assertTrue(file.exists());
        assertEquals(64496, file.length());

    }
}
