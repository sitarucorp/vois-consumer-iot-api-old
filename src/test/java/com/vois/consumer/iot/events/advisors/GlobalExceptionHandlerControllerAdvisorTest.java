package com.vois.consumer.iot.events.advisors;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class GlobalExceptionHandlerControllerAdvisorTest {

    @Mock
    MockMvc mockMvc;

    @Ignore
    public void testHandleEventsException() throws Exception {
        mockMvc.perform(get("/iot/event/"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("ERROR: A technical exception occurred"));
    }

    @Test
    @Ignore
    public void testHandleApplicationError() throws Exception{
        mockMvc.perform(get("/iot/event/"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("ERROR: A technical exception occurred"));
    }

    @Test
    @Ignore
    public void testHandleFileNotFoundException() throws Exception{
        mockMvc.perform(get("/iot/event/"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("ERROR: no data file found"));
    }

    @Test
    @Ignore
    public void testHandleResourceNotFound() throws Exception {
        mockMvc.perform(get("/iot/event/"))
                .andExpect(status().isNotFound())
                .andExpectAll(content().string("ERROR: Device could not be located"),
                        content().string("ERROR: Id {productId} not found"));
    }
}
