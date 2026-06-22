package com.techpalle.exception;

public class OverdraftLimitExceededException extends BankingException{ 


	  public OverdraftLimitExceededException(String errorCode, String message) {
	        super(errorCode, message);
	    }


}
