package com.example.oauthtest.controller;

import com.example.oauthtest.oauth.apple.AppleOIDCTokenUtils;
import com.example.oauthtest.oauth.dto.AppleOAuthLoginPrincipalDto;
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
    AppleOAuthLoginPrincipalDto result = appleOIDCTokenUtils.getClaimsBy(appleOidcToken);

    System.out.println("애플 유저 섭젝트 : " + result.getSub());
    System.out.println("애플 유저 이메일 : " + result.getEmail());
  }// func

}
