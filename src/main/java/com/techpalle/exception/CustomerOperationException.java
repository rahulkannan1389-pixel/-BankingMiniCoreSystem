package com.techpalle.exception;

public class CustomerOperationException extends BankingException{

	 public CustomerOperationException(String errorCode, String message) {
	        super(errorCode, message);
	    }


}
