package com.vw.exceptions;

public class ListOfCarIsEmptyException extends RuntimeException {
    public ListOfCarIsEmptyException(String message){
        super(message);
    }
}
