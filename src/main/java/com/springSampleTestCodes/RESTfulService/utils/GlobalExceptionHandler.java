package com.springSampleTestCodes.RESTfulService.utils;

import com.springSampleTestCodes.RESTfulService.exception.NotFoundException;
import com.springSampleTestCodes.RESTfulService.payload.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
    ApiError apiError = ApiError.setApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    ApiError apiError = ApiError.setApiError(HttpStatus.BAD_REQUEST, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ex);
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<Object> handleUsernameNotFoundException(NotFoundException ex) {
    ApiError apiError = ApiError.setApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    ApiError apiError = ApiError.setApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    return buildResponseEntity(apiError);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
