package com.mav.assertrest.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

  public static ValidationException ofTitleRequired() {
    return new ValidationException("title is required");
  }

  public ValidationException(final String reason) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
  }
}
