package com.app.crypto.model.binance;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BinanceStreamMessage {

  @JsonProperty("stream")
  private String stream;

  @JsonProperty("data")
  private BinanceTicker data;

}
