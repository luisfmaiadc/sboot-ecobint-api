package com.portfolio.luisfmdc.sboot_ecobint_api.config.exception;

public class BinNotFoundException extends RuntimeException {
    public BinNotFoundException(String message) {
        super(message);
    }
}