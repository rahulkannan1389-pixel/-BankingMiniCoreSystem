package com.techpalle.exception;

public class InvalidDataException extends BankingException{

  public InvalidDataException(String errorCode, String message) {
        super(errorCode, message);
    }


}
