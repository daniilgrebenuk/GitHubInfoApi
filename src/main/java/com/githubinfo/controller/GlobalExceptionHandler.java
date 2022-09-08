package com.githubinfo.controller;

import com.githubinfo.dto.UserNotFoundDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler()
  public ResponseEntity<UserNotFoundDto> gitHubUserNotFound(RuntimeException exception) {
    return new ResponseEntity<>(new UserNotFoundDto(404, exception.getMessage()), HttpStatus.NOT_FOUND);
  }
}
