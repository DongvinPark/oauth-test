package com.example.oauthtest.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleIdTokenVerifierConfig {

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String CLIENT_ID;

  @Bean
  public GoogleIdTokenVerifier createGoogleIdTokenVerifier(){
    return new GoogleIdTokenVerifier
        .Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(List.of(CLIENT_ID)).build();
  }

}
