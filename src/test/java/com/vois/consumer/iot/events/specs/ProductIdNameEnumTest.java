package com.vois.consumer.iot.events.specs;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ProductIdNameEnumTest {

    @Test
    public void testGetProductNameByProductId() {
        assertEquals("GeneralTracker" , ProductIdNameEnum.getProductNameByProductId("6900001001"));
        assertEquals("GeneralTracker" , ProductIdNameEnum.getProductNameByProductId("692838W001"));
        assertEquals("GeneralTracker" , ProductIdNameEnum.getProductNameByProductId("69QWERTY01"));

        assertEquals("CyclePlusTracker" , ProductIdNameEnum.getProductNameByProductId("WG11155638"));
        assertEquals("CyclePlusTracker" , ProductIdNameEnum.getProductNameByProductId("WGQWERTY38"));
        assertEquals("CyclePlusTracker" , ProductIdNameEnum.getProductNameByProductId("WG00055638"));

        assertEquals("Unknown" , ProductIdNameEnum.getProductNameByProductId("GW00055638"));
        assertEquals("Unknown" , ProductIdNameEnum.getProductNameByProductId("9600055638"));
        assertEquals("Unknown" , ProductIdNameEnum.getProductNameByProductId("QWERTY0638"));
        assertEquals("Unknown" , ProductIdNameEnum.getProductNameByProductId("PWG1155638"));
    }
}
