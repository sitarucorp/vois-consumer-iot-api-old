package com.vois.consumer.iot.events.advisors;

import com.vois.consumer.iot.events.dto.ErrorResponse;
import com.vois.consumer.iot.events.exceptions.ConsumerEventsApplicationException;
import com.vois.consumer.iot.events.exceptions.ConsumerEventsResourceNotFoundException;
import com.vois.consumer.iot.events.exceptions.NoConsumerEventSourceDataFileFoundException;
import com.vois.consumer.iot.events.specs.ResponseDescriptionMessageEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@NoArgsConstructor
@Slf4j
public class GlobalExceptionHandlerControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class ,
            IllegalStateException.class , Exception.class})
    public ResponseEntity<ErrorResponse> handleEventsException(RuntimeException ex , WebRequest request) {
        log.error("handleEventsException : {}" , ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ResponseDescriptionMessageEnum.ERROR_TECHNICAL_EXCEPTION_OCCURED.getDescription());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler({ConsumerEventsApplicationException.class})
    public ResponseEntity<ErrorResponse> handleApplicationError(ConsumerEventsApplicationException ex) {
        log.error("handleApplicationError : {}" , ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ResponseDescriptionMessageEnum.ERROR_TECHNICAL_EXCEPTION_OCCURED.getDescription());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler({ConsumerEventsResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ConsumerEventsResourceNotFoundException ex) {
        log.error("handleResourceNotFound : {}" , ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ResponseDescriptionMessageEnum.replaceResourceValueGetDescription(
                        ResponseDescriptionMessageEnum.ERROR_PRODUCT_ID_NOT_FOUND , ex.getResource())));
    }

    @ExceptionHandler({NoConsumerEventSourceDataFileFoundException.class})
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(NoConsumerEventSourceDataFileFoundException ex) {
        log.error("handleFileNotFoundException : {}" , ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ResponseDescriptionMessageEnum.ERROR_NO_DATA_FOUND.getDescription());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
