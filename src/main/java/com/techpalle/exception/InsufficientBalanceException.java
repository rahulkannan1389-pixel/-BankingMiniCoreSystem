package com.techpalle.exception;

public class InsufficientBalanceException extends BankingException {

 public InsufficientBalanceException(String errorCode, String message) {
        super(errorCode, message);
    }
}

