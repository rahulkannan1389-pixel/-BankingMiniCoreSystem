package com.techpalle.exception;

public class DuplicateResourceException  extends BankingException {

	 public DuplicateResourceException(String errorCode, String message) {
	        super(errorCode, message);
	    }


}
