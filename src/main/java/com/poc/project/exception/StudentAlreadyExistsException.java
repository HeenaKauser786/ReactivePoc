package com.poc.project.exception;

public class StudentAlreadyExistsException extends Exception{
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
