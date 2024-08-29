package com.example.calendarapp.exception;

//  커스텀 Exception
public class InvalidEventException extends RuntimeException {

    public InvalidEventException(String message) {
        super(message);
    }
}
