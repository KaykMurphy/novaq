package com.novaq.exceptions;

import com.novaq.dtos.response.StandarErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandarErrorDTO> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request){

        StandarErrorDTO errorResponse = new StandarErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandarErrorDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request){

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = String.join(", ", errors);

        StandarErrorDTO errorResponse = new StandarErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(WebhookProcessingException.class)
    public ResponseEntity<Void> handleWebhookException(WebhookProcessingException e){
        log.error("Silent error in webhook intercepted by handler: {}", e.getMessage());

        return ResponseEntity.ok().build();
    }


}
