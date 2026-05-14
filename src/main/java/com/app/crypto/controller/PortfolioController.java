package com.app.crypto.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.crypto.model.PortfolioEntry;
import com.app.crypto.service.PortfolioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

  private final PortfolioService portfolioService;

  @GetMapping
  public ResponseEntity<List<PortfolioEntry>> getPortfolio() {

    return ResponseEntity.ok(portfolioService.getPortfolio());
  }

}
