package com.example.oauthtest.controller;

import com.example.oauthtest.oauth.apple.AppleOIDCTokenUtils;
import com.example.oauthtest.oauth.dto.ApplePublicKeyResponse;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestAppleOidcTokenController {

  private final AppleOIDCTokenUtils appleOIDCTokenUtils;

  @GetMapping("/test-apple-public-key")
  public void testApplePublicKey(
      @RequestParam String appleOidcToken
  ) {
    Claims claims = appleOIDCTokenUtils.getClaimsBy(appleOidcToken);

    String payload = claims.getSubject();

    System.out.println(payload);
  }// func

}
