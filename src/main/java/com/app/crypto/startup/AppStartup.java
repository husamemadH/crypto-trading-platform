package com.app.crypto.startup;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.app.crypto.service.BinanceWebSocketService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppStartup implements ApplicationListener<ApplicationReadyEvent> {

  private final BinanceWebSocketService binanceWebSocketService;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    binanceWebSocketService.connect();
  }
}
