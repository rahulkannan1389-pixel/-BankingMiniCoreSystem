package com.techpalle.exception;

public class AccountOperationException extends BankingException{


    public AccountOperationException(String errorCode, String message) {
        super(errorCode, message);
    }

    public AccountOperationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }



}
