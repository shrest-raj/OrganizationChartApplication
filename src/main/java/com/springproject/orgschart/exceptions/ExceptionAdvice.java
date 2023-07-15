package com.springproject.orgschart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(DataNotFoundException dataNotFoundException) {
        return new ResponseEntity<String>("Sorry, We have no data to display, please post some data first!!!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        return new ResponseEntity<String>("The given id is not present in DB.\nPlease change your request!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameNotNullException.class)
    public ResponseEntity<String> handleNameNotNullException(NameNotNullException nameNotNullException) {
        return new ResponseEntity<String>("Input name field is empty or invalid.\nPlease look into it!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidManagerIdException.class)
    public ResponseEntity<String> handleInvalidManagerIdException(InvalidManagerIdException invalidManagerIdException) {
        return new ResponseEntity<String>("Your entered Manager ID is out of bounds.\nPlease look into it!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidManagerLevelException.class)
    public ResponseEntity<String> handleInvalidManagerLevelException(InvalidManagerLevelException invalidManagerLevelException) {
        return new ResponseEntity<String>("Your entered Manager ID is invalid because a person can't report to a person with designation either as same or lower in order.\nAlso please check if the level difference between manager and employee is equal to 1.\nPlease look into it!", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateEmployeeException.class)
    public ResponseEntity<String> handleDuplicateEmployeeException(DuplicateEmployeeException duplicateEmployeeException) {
        return new ResponseEntity<String>("Sorry,Employee with same credentials already exists. \nPlease try again!", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleDuplicateEmployeeException(EmployeeNotFoundException employeeNotFoundException) {
        return new ResponseEntity<String>("Employee with the given ID not found. \nPlease try again !", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MultipleDirectorException.class)
    public ResponseEntity<String> handleMultipleDirectorException(MultipleDirectorException multipleDirectorException) {
        return new ResponseEntity<String>("Multiple Directors cannot exist in an organization. \nPlease try again !", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NameAmbiguityException.class)
    public ResponseEntity<String> handleNameAmbiguityException(NameAmbiguityException nameAmbiguityException) {
        return new ResponseEntity<String>("As replace is set false, the name of the employee cannot change. \nPlease try again !", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DirectorReplacementException.class)
    public ResponseEntity<String> handleDirectorReplacementException(DirectorReplacementException directorReplacementException) {
        return new ResponseEntity<String>("Director cannot be replaced when the replace field is set false try setting it to true. \nPlease try again !", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeIdNotValidException.class)
    public ResponseEntity<String> handleEmployeeIdNotFoundException(EmployeeIdNotValidException employeeIdNotFoundException) {
        return new ResponseEntity<String>("Employee Id is not valid \nPlease try again !", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJobTitleException.class)
    public ResponseEntity<String> handleInvalidJobTitleException(InvalidJobTitleException invalidJobTitleException) {
        return new ResponseEntity<String>("Employee JobTitle is not valid \nPlease try again !", HttpStatus.BAD_REQUEST);
    }

}
