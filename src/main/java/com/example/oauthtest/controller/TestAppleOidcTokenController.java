package com.example.oauthtest.controller;

import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestAppleOidcTokenController {

  @GetMapping("/test-apple-public-key")
  public ResponseEntity<Object> testApplePublicKey()
      throws IOException {
    URL url = new URL("https://appleid.apple.com/auth/keys");

    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");

    BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "UTF-8")
    );
    StringBuilder responseBuilder = new StringBuilder();
    String responseLine = null;
    while( (responseLine = br.readLine()) != null ){
      responseBuilder.append(responseLine.trim());
    }

    return ResponseEntity.ok().body(responseBuilder.toString());
  }

}
