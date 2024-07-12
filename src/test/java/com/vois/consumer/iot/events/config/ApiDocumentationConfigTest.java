
package com.vois.consumer.iot.events.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ApiDocumentationConfigTest {
    @Test
    public void testBasic() {
        ApiDocumentationConfig apiDocumentationConfig = new ApiDocumentationConfig();
        OpenAPI info = apiDocumentationConfig.openAPI();
        assertNotNull(info);
    }
}
