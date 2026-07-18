package com.novaq.exceptions;

import com.novaq.dtos.response.StandarErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandarErrorDTO> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request){

        StandarErrorDTO errorResponse = new StandarErrorDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Access denied.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<StandarErrorDTO> handleDisabledException(DisabledException e, HttpServletRequest request){

        StandarErrorDTO errorResponse = new StandarErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Account is disabled. Contact the administrator.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandarErrorDTO> handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request){

        StandarErrorDTO errorResponse = new StandarErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "User not found.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }





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

        List<String> errors = new java.util.ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(fe ->
                errors.add(fe.getDefaultMessage()));

        e.getBindingResult().getGlobalErrors().forEach(ge ->
                errors.add(ge.getDefaultMessage()));

        String errorMessage = String.join(", ", errors);

        StandarErrorDTO errorResponse = new StandarErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandarErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request){

        StandarErrorDTO errorResponse = new StandarErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "A data integrity constraint was violated. Check for duplicate values.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(WebhookProcessingException.class)
    public ResponseEntity<Void> handleWebhookException(WebhookProcessingException e){
        log.error("Silent error in webhook intercepted by handler: {}", e.getMessage());

        return ResponseEntity.ok().build();
    }


}
