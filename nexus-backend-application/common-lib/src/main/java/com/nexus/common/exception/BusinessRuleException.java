package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when business rule is violated (422)
 */
public class BusinessRuleException extends ClientException {
    
    public BusinessRuleException(String message) {
        super(message, "BUSINESS_RULE_VIOLATION", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

