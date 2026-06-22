package com.techpalle.exception;

public class TransactionConflictException extends BankingException{
	

public TransactionConflictException(String errorCode, String message) {
        super(errorCode, message);
    }
}
