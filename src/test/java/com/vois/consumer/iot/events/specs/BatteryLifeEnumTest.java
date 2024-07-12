package com.vois.consumer.iot.events.specs;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class BatteryLifeEnumTest {

    @Test
    public void printBatteryLifeOnConsoleForDemo() {
        float[] samples = new float[]{
                0.97F , 0.98F , 0.99F , 0.88F , 0.91F , 0.34F , 0.63F , 0.28F , 0.41F , 0.0F , 0.09F , 0.51F , 0.40F , 0.60F , 0.98F , 0.10F
        };
        for(float sample : samples) {
            log.info("Battery life detected for sample value of '{}' is '{}' " , sample ,
                    BatteryLifeEnum.getBatteryLife(sample));
        }
    }

    @Test
    public void testGetBatteryLifeNegativeScenario() {
        assertEquals("Unknown" , BatteryLifeEnum.getBatteryLife(0.0F));
        assertEquals("Unknown" , BatteryLifeEnum.getBatteryLife(-0.9F));
        assertEquals("Unknown" , BatteryLifeEnum.getBatteryLife(""));
    }

    @Test
    public void testGetBatteryLifeWhenBatteryIsCritical() {
        assertEquals("Critical" , BatteryLifeEnum.getBatteryLife(0.03F));
        assertEquals("Critical" , BatteryLifeEnum.getBatteryLife(0.09F));
        assertNotEquals("Critical" , BatteryLifeEnum.getBatteryLife(0.10F));
    }

    @Test
    public void testGetBatteryLifeWhenBatteryIsLow() {
        assertEquals("Low" , BatteryLifeEnum.getBatteryLife(0.10F));
        assertEquals("Low" , BatteryLifeEnum.getBatteryLife(0.28F));
        assertEquals("Low" , BatteryLifeEnum.getBatteryLife(0.34F));
    }

    @Test
    public void testGetBatteryLifeWhenBatteryIsMedium() {
        assertEquals("Medium" , BatteryLifeEnum.getBatteryLife(0.51F));
        assertEquals("Medium" , BatteryLifeEnum.getBatteryLife(0.40F));
        assertEquals("Medium" , BatteryLifeEnum.getBatteryLife(0.41F));
    }

    @Test
    public void testGetBatteryLifeWhenBatteryIsHigh() {
        assertEquals("High" , BatteryLifeEnum.getBatteryLife(0.97F));
        assertEquals("High" , BatteryLifeEnum.getBatteryLife(0.91F));
        assertEquals("High" , BatteryLifeEnum.getBatteryLife(0.63F));
        assertEquals("High" , BatteryLifeEnum.getBatteryLife(0.60F));
        assertEquals("High" , BatteryLifeEnum.getBatteryLife(0.88F));
    }

    @Test
    public void testGetBatteryLifeWhenBatteryIsFull() {
        assertEquals("Full" , BatteryLifeEnum.getBatteryLife(0.98F));
        assertEquals("Full" , BatteryLifeEnum.getBatteryLife(0.9856F));
        assertEquals("Full" , BatteryLifeEnum.getBatteryLife(0.992F));
        assertNotEquals("Full" , BatteryLifeEnum.getBatteryLife(0.97F));
    }

}
