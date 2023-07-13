package com.example.oauthtest.config;

import com.example.oauthtest.oauth.apple.ApplePublicKeyProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplePublicKeyProviderConfig {

  @Bean
  public ApplePublicKeyProvider getApplePublicKeyProvider() {
    return new ApplePublicKeyProvider();
  }

}
