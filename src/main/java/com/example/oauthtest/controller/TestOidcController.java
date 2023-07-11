package com.example.oauthtest.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestOidcController {

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String CLIENT_ID;

  @PostMapping("/test-oidc")
  public void testOidc(
      @RequestParam String tokenValue
  ) throws GeneralSecurityException, IOException {

    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(
            List.of(CLIENT_ID)
        ).build();

    GoogleIdToken idToken = verifier.verify(tokenValue);

    if(idToken != null){
      Payload payload = idToken.getPayload();

      // Print user identifier
      String userId = payload.getSubject();
      System.out.println("User ID: " + userId);

      // Get profile information from payload
      String email = payload.getEmail();
      System.out.println("email = " + email);

      boolean emailVerified = payload.getEmailVerified();
      System.out.println("emailVerified = " + emailVerified);

      String name = (String) payload.get("name");
      System.out.println("name = " + name);

      String pictureUrl = (String) payload.get("picture");
      System.out.println("pictureUrl = " + pictureUrl);

      String locale = (String) payload.get("locale");
      System.out.println("locale = " + locale);

      String familyName = (String) payload.get("family_name");
      System.out.println("familyName = " + familyName);

      String givenName = (String) payload.get("given_name");
      System.out.println("givenName = " + givenName);
    } else {
      System.out.println("아이디토큰이 널임!!");
    }

  }

}
