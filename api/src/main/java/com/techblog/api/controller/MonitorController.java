package com.techblog.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

  @GetMapping("/ping")
  public ResponseEntity healthCheck() {
    return ResponseEntity.ok("pong");
  }

}
