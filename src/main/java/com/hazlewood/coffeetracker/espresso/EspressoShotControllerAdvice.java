package com.hazlewood.coffeetracker.espresso;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EspressoShotControllerAdvice {

  @ResponseBody
  @ExceptionHandler(EspressoShotNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String beansNotFoundHandler(EspressoShotNotFoundException ex) {
    return ex.getMessage();
  }
}
