package com.vois.consumer.iot.events.specs;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseDescriptionMessageEnumTest {

    @Test
    public void testReplaceResourceValueGetDescription() {
        assertEquals("ERROR: Id WG9834859383 not found" ,
                ResponseDescriptionMessageEnum.replaceResourceValueGetDescription(ResponseDescriptionMessageEnum.ERROR_PRODUCT_ID_NOT_FOUND , "WG9834859383"));
        assertEquals("ERROR: Id 690001278374 not found" ,
                ResponseDescriptionMessageEnum.replaceResourceValueGetDescription(ResponseDescriptionMessageEnum.ERROR_PRODUCT_ID_NOT_FOUND , "690001278374"));
    }
}
