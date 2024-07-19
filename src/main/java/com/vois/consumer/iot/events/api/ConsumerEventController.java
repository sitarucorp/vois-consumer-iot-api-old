package com.vois.consumer.iot.events.api;

import com.vois.consumer.iot.events.dto.BasicResponse;
import com.vois.consumer.iot.events.dto.ConsumerEvent;
import com.vois.consumer.iot.events.dto.ErrorResponse;
import com.vois.consumer.iot.events.dto.LoadConsumerEventRequest;
import com.vois.consumer.iot.events.dto.LoadConsumerEventResponse;
import com.vois.consumer.iot.events.dto.ProductResponse;
import com.vois.consumer.iot.events.dto.VersionInfo;
import com.vois.consumer.iot.events.exceptions.ConsumerEventsApplicationException;
import com.vois.consumer.iot.events.exceptions.ConsumerEventsResourceNotFoundException;
import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import com.vois.consumer.iot.events.service.DataLoadingServiceImpl;
import com.vois.consumer.iot.events.service.SearchConsumerEventServiceImpl;
import com.vois.consumer.iot.events.specs.ResponseDescriptionMessageEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ConsumerEventController {
    @Value("${version}")
    private String appVersion;
    private final DataLoadingServiceImpl dataLoadingService;
    private final SearchConsumerEventServiceImpl searchConsumerEventService;

    public ConsumerEventController(DataLoadingServiceImpl dataLoadingService ,
                                   SearchConsumerEventServiceImpl searchConsumerEventService) {
        this.dataLoadingService = dataLoadingService;
        this.searchConsumerEventService = searchConsumerEventService;
    }

    @Operation(summary = "Returns version information configured in property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Version Information",
                    content = {@Content(mediaType = "application/json")})})
    @GetMapping(path = "/version")
    public ResponseEntity<VersionInfo> getVersionInfo() {
        return ResponseEntity.ok(VersionInfo.builder().version(appVersion).build());
    }


    @Operation(summary = "Returns Most recent event once the product is identified nearest to the timestamp provided",
            description = "In a case when timestamp is not provided, API will use UTC current time and find the nearest event available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data available at backend for given productId") ,
            @ApiResponse(responseCode = "400", description = "When device data is not available") ,
            @ApiResponse(responseCode = "500", description = "When server runs into technical difficulties")})
    @GetMapping(path = "/iot/event/v1")
    public ResponseEntity<BasicResponse> getProduct(@RequestParam("ProductId") String productId ,
                                                    @RequestParam(value = "tstmp", required = false) String timestamp)

            throws ConsumerEventsResourceNotFoundException {
        try {
            // searching the productId whether we have a data available or not
            Optional<ConsumerEvent> searchResult = searchConsumerEventService.searchProductEvent(productId , Optional.ofNullable(timestamp));

            // if data available then return otherwise an error response
            ResponseEntity<BasicResponse> response = searchResult.<ResponseEntity<BasicResponse>>map(consumerEvent -> ResponseEntity.ok(
                            ProductResponse.toProductResponse(consumerEvent))).
                    orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ErrorResponse(ResponseDescriptionMessageEnum.replaceResourceValueGetDescription(
                                    ResponseDescriptionMessageEnum.ERROR_PRODUCT_ID_NOT_FOUND , productId))));

            return response.getBody() instanceof ProductResponse && ((ProductResponse) response.getBody())
                    .getDescription().contains("Device could not be located")
                    ? ResponseEntity.badRequest().body(response.getBody()) : response;    // patching

        } catch (Exception e) {
            throw new ConsumerEventsResourceNotFoundException(productId);
        }
    }

    @Operation(summary = "This API instruct the backend to refresh the data based on given filepath")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Reloading the data",
                    content = {@Content(mediaType = "application/json")})})
    @PostMapping("/iot/event/v1")
    public ResponseEntity<LoadConsumerEventResponse> loadConsumerEvent(@RequestBody LoadConsumerEventRequest loadConsumerEventRequest)
            throws ConsumerEventsApplicationException, NoConsumerEventSourceDataFileFoundException {
        try {
            //validateFilePathMatchesOsScheme(loadConsumerEventRequest.getFilepath());  // confirm unix or windows filepath provided
            Optional<Integer> result = dataLoadingService.refreshDataFromFile(loadConsumerEventRequest.getFilepath());

            if(result.isPresent()) {
                ResponseEntity response = ResponseEntity
                        .ok(LoadConsumerEventResponse.builder().description(
                                ResponseDescriptionMessageEnum.DATA_REFRESHED.getDescription()).build());
                return response;
                } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(LoadConsumerEventResponse.builder().description(
                                ResponseDescriptionMessageEnum.ERROR_NO_DATA_FOUND.getDescription()).build());
            }
        } catch (NoConsumerEventSourceDataFileFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ConsumerEventsApplicationException("Error occured while processing the file - path:"
                    + loadConsumerEventRequest.getFilepath() + " e=" + e.getMessage());
        }
    }
}
