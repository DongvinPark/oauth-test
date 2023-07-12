package com.example.oauthtest.oauth.google;

import com.nimbusds.common.contenttype.ContentType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication
  ) throws IOException, ServletException {

    OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

    String jsonInputString =
        "{"
            + "\n\t\"accessToken\" : \"" + oidcUser.getSubject() + "\""
        + "\n}";
    byte[] input = jsonInputString.getBytes("UTF-8");

    response.setContentType("application/json");
    response.getOutputStream().write(input, 0, input.length);

    /*
    //url을 이용해서 POST 연결 설정하기
    URL url = new URL("http://localhost:8081/jwt");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);

    //응답으로 보낼 JSON 만들고 POST 전송하기
    String jsonInputString = "{"
        + "\n\t\"accessToken\" : \""+ oidcUser.getSubject() + "\"\n}";
    OutputStream os = con.getOutputStream();
    byte[] input = jsonInputString.getBytes("UTF-8");
    os.write(input, 0, input.length);

    //상대 서버로부터 응답 확인하기
    BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "UTF-8")
    );
    StringBuilder responseBuilder = new StringBuilder();
    String responseLine = null;
    while( (responseLine = br.readLine()) != null ){
      responseBuilder.append(responseLine.trim());
    }

    System.out.println("응답! : " + responseBuilder.toString());

    // 시스템 리소스 반납
    os.close();
    con.disconnect();*/
  }
}
