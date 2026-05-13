
package com.app.crypto.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "crypto")
@Component
@Data
public class CryptoConfig {

  private List<String> symbols;

}
