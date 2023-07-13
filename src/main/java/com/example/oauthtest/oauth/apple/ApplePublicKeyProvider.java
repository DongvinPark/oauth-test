package com.example.oauthtest.oauth.apple;

import com.example.oauthtest.oauth.dto.ApplePublicKey;
import com.example.oauthtest.oauth.dto.ApplePublicKeyResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class ApplePublicKeyProvider {

  public ApplePublicKeyResponse getApplePublicKey() throws IOException {
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
    while ((responseLine = br.readLine()) != null) {
      responseBuilder.append(responseLine.trim());
    }

    Gson gson = new Gson();

    Map<String, List<String>> originMap = gson.fromJson(responseBuilder.toString(), Map.class);

    List<String> stringList = originMap.get("keys");

    ApplePublicKeyResponse applePublicKeyResponse = new ApplePublicKeyResponse(
        new ArrayList<>()
    );

    for (Object o : stringList) {
      String targetString = o.toString();
      //System.out.println("원본 키리스펀스 스트링 = " + targetString);

      Map<String, String> keyMap = gson.fromJson(targetString, Map.class);

      ApplePublicKey applePublicKey = new ApplePublicKey();

      applePublicKey.setKty(keyMap.get("kty"));
      applePublicKey.setKid(keyMap.get("kid"));
      applePublicKey.setUse(keyMap.get("use"));
      applePublicKey.setAlg(keyMap.get("alg"));
      applePublicKey.setN(keyMap.get("n"));
      applePublicKey.setE(keyMap.get("e"));

      applePublicKeyResponse.getKeys().add(applePublicKey);
    }// O for

    /*System.out.println("만들어진 applePublicKeyResponse 점검!");
    System.out.println("키 개수 : " + applePublicKeyResponse.getKeys().size());
    for(ApplePublicKey key : applePublicKeyResponse.getKeys()){
      System.out.println("키 출력시작!!");
      System.out.println(key.getKty());
      System.out.println(key.getKid());
      System.out.println(key.getUse());
      System.out.println(key.getAlg());
      System.out.println(key.getN());
      System.out.println(key.getE());
    }*/

    return applePublicKeyResponse;
  }

}
