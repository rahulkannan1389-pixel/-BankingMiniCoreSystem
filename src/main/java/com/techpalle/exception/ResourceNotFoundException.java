package com.techpalle.exception;

public class ResourceNotFoundException extends BankingException {
	
    public ResourceNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }
}
