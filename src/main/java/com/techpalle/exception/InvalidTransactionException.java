package com.techpalle.exception;

public class InvalidTransactionException extends BankingException {

public InvalidTransactionException(String errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidTransactionException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }


}
