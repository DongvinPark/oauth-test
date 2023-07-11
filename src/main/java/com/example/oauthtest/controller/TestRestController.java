package com.example.oauthtest.controller;

import com.example.oauthtest.controller.dto.LoginSuccessDtoOut;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestRestController {

  @GetMapping("/login/oauth2/code/google")
  public ResponseEntity<LoginSuccessDtoOut> loginSuccess(
      @RequestParam(required = false) String state,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String scope,
      @RequestParam(required = false) String authuser,
      @RequestParam(required = false) String prompt,
      Authentication authentication
  ){
    System.out.println("변경된 URL 호출!!");
    OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

    OidcIdToken oidcIdToken = oidcUser.getIdToken();

    System.out.println("에스엔에스아이디 : " + oidcIdToken.getSubject());
    System.out.println("에스엔에스이메일 : " + oidcIdToken.getEmail());

    String sampleJWT = "sampleJWT";

    return ResponseEntity.status(HttpStatus.OK).body(
        LoginSuccessDtoOut.builder()
            .accessToken(sampleJWT)
            .build()
    );
  }

  @GetMapping("/test-oidc")
  public ResponseEntity<LoginSuccessDtoOut> login(
      @RequestParam String tokenValue
  ){
    Map<String, Object> claims = new HashMap<>();

    claims.put("name", "DongvinPark");

    OidcIdToken oidcIdToken = new OidcIdToken(tokenValue, null, null, claims);

    System.out.println("오아이디씨토큰 직접 생성 후 섭젝트 확인 : " + oidcIdToken.getSubject());
    System.out.println("오아이디씨토큰 직접 생성 후 이메일 확인 : " + oidcIdToken.getEmail());

    String sampleJWT = "sampleJWT";

    return ResponseEntity.status(HttpStatus.OK).body(
        LoginSuccessDtoOut.builder()
            .accessToken(sampleJWT)
            .build()
    );
  }

}
