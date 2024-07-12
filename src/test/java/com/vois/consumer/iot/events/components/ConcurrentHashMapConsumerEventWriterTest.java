package com.vois.consumer.iot.events.components;


import com.vois.consumer.iot.events.dto.ConsumerEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.Chunk;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@SpringJUnitConfig
@RunWith(SpringJUnit4ClassRunner.class)
public class ConcurrentHashMapConsumerEventWriterTest {
    // ArgumentCaptor captor = ArgumentCaptor.forClass(EventDataCarrier.class);

    @Test
    public void testWriteChunksSavedWell() throws Exception {
        EventDataCarrier eventDataCarrier = new EventDataCarrier(new ConcurrentHashMap() , new ConcurrentHashMap<>());
        ConcurrentHashMapConsumerEventWriter eventWriter = new ConcurrentHashMapConsumerEventWriter(eventDataCarrier);
        eventWriter.write(new Chunk<>(
                ConsumerEvent.builder().eventId("1").build() ,
                ConsumerEvent.builder().eventId("2").build() ,
                ConsumerEvent.builder().eventId("3").build() ,
                ConsumerEvent.builder().eventId("4").build()
        ));
        assertEquals(4 , eventWriter.getEventDataCarrier().getNumberOfRecordsInMemory());
    }
}
