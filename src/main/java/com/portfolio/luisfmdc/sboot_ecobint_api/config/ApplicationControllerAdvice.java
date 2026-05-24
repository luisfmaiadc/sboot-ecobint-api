package com.portfolio.luisfmdc.sboot_ecobint_api.config;

import com.portfolio.luisfmdc.ecobint.infrastructure.dto.StandardError;
import com.portfolio.luisfmdc.sboot_ecobint_api.config.exception.BinNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<StandardError> handleException(Exception ex) {
        StandardError error = new StandardError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        log.error("[ApplicationControllerAdvice] handleException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BinNotFoundException.class)
    public ResponseEntity<StandardError> handleBinNotFound(BinNotFoundException ex) {
        StandardError error = new StandardError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        log.error("[ApplicationControllerAdvice] handleBinNotFound: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}