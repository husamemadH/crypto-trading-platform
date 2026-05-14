package com.app.crypto.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.crypto.dto.request.PriceAlertRequest;
import com.app.crypto.model.PriceAlert;
import com.app.crypto.service.PriceAlertService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class PriceAlertController {

  private final PriceAlertService priceAlertService;

  @PostMapping
  public ResponseEntity<PriceAlert> createAlert(@Valid @RequestBody PriceAlertRequest request) {

    PriceAlert alert = priceAlertService.createAlert(request);

    return ResponseEntity.ok(alert);

  }

  @GetMapping
  public ResponseEntity<List<PriceAlert>> getAlerts() {

    return ResponseEntity.ok().body(priceAlertService.getAllAlerts());
  }
}
