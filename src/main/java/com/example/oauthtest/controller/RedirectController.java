package com.example.oauthtest.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedirectController {

  public void loginRedirect(String jwt) throws IOException {
    //url을 이용해서 POST 연결 설정하기
    URL url = new URL("http://localhost:8081/jwt");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);
    
    //응답으로 보낼 JSON 만들고 POST 전송하기
    String jsonInputString = "{"
        + "\n\t\"accessToken\" : \""+ jwt + "\"\n}";
    OutputStream os = con.getOutputStream();
    byte[] input = jsonInputString.getBytes("UTF-8");
    os.write(input, 0, input.length);
    
    //상대 서버로부터 응답 확인하기
    BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "UTF-8")
    );
    StringBuilder response = new StringBuilder();
    String responseLine = null;
    while( (responseLine = br.readLine()) != null ){
      response.append(responseLine.trim());
    }
    
    System.out.println("응답! : " + response.toString());
    
    // 시스템 리소스 반납
    os.close();
    con.disconnect();
  }

}
