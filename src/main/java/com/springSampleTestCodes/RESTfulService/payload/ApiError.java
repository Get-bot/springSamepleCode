package com.springSampleTestCodes.RESTfulService.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ApiError {
  private HttpStatus status;
  private String message;
  private String debugMessage;
  private LocalDateTime timestamp;

  public static ApiError setApiError(HttpStatus status, String message, Throwable ex) {
    ApiError apiError = new ApiError();
    apiError.status = status;
    apiError.message = message;
    apiError.debugMessage = ex.getLocalizedMessage();
    apiError.timestamp = LocalDateTime.now();
    return apiError;
  }
}
