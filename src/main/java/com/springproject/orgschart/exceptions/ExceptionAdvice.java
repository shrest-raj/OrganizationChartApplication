package com.springproject.orgschart.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(DataNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorMessage("Sorry, We have no data to display, please post some data first!!!");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorMessage("The given id is not present in DB. Please change your request!");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameNotNullException.class)
    public ResponseEntity<ErrorResponse> handleNameNotNullException(NameNotNullException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorMessage("Input name field is empty or invalid. Please look into it!");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidManagerIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidManagerIdException(InvalidManagerIdException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorMessage("Your entered Manager ID is out of bounds. Please look into it!");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidManagerLevelException.class)
    public ResponseEntity<ErrorResponse> handleInvalidManagerLevelException(InvalidManagerLevelException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.CONFLICT.value());
        errorResponse.setErrorMessage("Your entered Manager ID is invalid because a person can't report to a person with designation either as same or lower in order.\nAlso please check if the level difference between manager and employee is equal to 1.\nPlease look into it!");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateEmployeeException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmployeeException(DuplicateEmployeeException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.CONFLICT.value());
        errorResponse.setErrorMessage("Sorry,Employee with same credentials already exists. Please try again!");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setErrorMessage("Employee with the given ID not found. Please try again !");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MultipleDirectorException.class)
    public ResponseEntity<ErrorResponse> handleMultipleDirectorException(MultipleDirectorException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.CONFLICT.value());
        errorResponse.setErrorMessage("Multiple Directors cannot exist in an organization. Please try again !");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NameAmbiguityException.class)
    public ResponseEntity<ErrorResponse> handleNameAmbiguityException(NameAmbiguityException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorMessage("As replace is set false, the name of the employee cannot change. Please try again !");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DirectorReplacementException.class)
    public ResponseEntity<ErrorResponse> handleDirectorReplacementException(DirectorReplacementException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorMessage("Director cannot be replaced when the replace field is set false. Try setting it to true. Please try again !");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


@ExceptionHandler(EmployeeIdNotValidException.class)
public ResponseEntity<ErrorResponse> handleEmployeeIdNotFoundException(EmployeeIdNotValidException ex, HttpServletRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
    errorResponse.setErrorMessage("Employee Id is not valid. Please try again!");
    errorResponse.setPath(request.getRequestURI());

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}



    @ExceptionHandler(InvalidJobTitleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJobTitleException(InvalidJobTitleException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setErrorMessage("Employee JobTitle is not valid. Please try again!");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
