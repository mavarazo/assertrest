package com.mav.assertrest.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class InfoController {

  @GetMapping("/info")
  public ResponseEntity<String> getInfo() {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
