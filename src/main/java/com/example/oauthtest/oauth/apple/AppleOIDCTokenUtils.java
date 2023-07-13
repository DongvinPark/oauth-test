package com.example.oauthtest.oauth.apple;

import com.example.oauthtest.oauth.dto.AppleOAuthLoginPrincipalDto;
import com.example.oauthtest.oauth.dto.ApplePublicKey;
import com.example.oauthtest.oauth.dto.ApplePublicKeyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class AppleOIDCTokenUtils {

  private final ApplePublicKeyProvider applePublicKeyProvider;

  public AppleOAuthLoginPrincipalDto getClaimsBy(String identityToken) {

    try {
      // 애플의 아이디 서버로부터 공개키를 받아온다.
      // 애플이 준 공개키 값을 이용해서 ApplePublicKeyResponse를 구현하는 일은 결국 내가 해야 한다.
      ApplePublicKeyResponse response = applePublicKeyProvider.getApplePublicKey();
      //System.out.println("애플 퍼블릭 키 리스펀스 획득 성공!!");

      // 아이디토큰으로부터 헤더 부분만을 잘라낸 후, 헤더값을 통해서 애플의 공개키 응답 중 무엇을
      // 아이디토큰 디코드용 공개키를 만드는 것에 사용할 것인지를 결정한다.
      String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
      //System.out.println("헤더 획득 성공!!");

      Map<String, String> header = new ObjectMapper().readValue(
          new String(
              Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"
          ), Map.class
      );
      //System.out.println("옵젝트 맵퍼 작동 성공!!");

      /*System.out.println("헤더 출력");
      for(String s : header.keySet()){
        System.out.println("헤더 키 = " + s + "/ 헤더 값 = " + header.get(s));
      }*/

      ApplePublicKey key = response.getMatchedKeyBy(
          header.get("kid"), header.get("alg")
      ).orElseThrow(
          () -> new NullPointerException("Failed get public key from apple's id server.")
      );
      /*System.out.println("일치하는 애플퍼블릭키 가져오기 성공!");
      System.out.println("key의 n값 = " + key.getN());
      System.out.println("key의 e값 = " + key.getE());*/

      // 공개키를 만든다.
      byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
      //System.out.println("nBytes = " + Arrays.toString(nBytes));
      byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());
      //System.out.println("eBytes = " + Arrays.toString(eBytes));
      //System.out.println("바이트배열 만들기 성공!");
      BigInteger n = new BigInteger(1, nBytes);
      BigInteger e = new BigInteger(1, eBytes);
      //System.out.println("빅인티저 만들기 성공!");

      RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
      //System.out.println("알에스에이퍼블릭키 인스턴스 만들기 성공!");
      KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
      //System.out.println("키팩토리 만들기 성공!");
      PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
      //System.out.println("디코드용 공개키 만들기 성공!!");

      // 만들어진 공개키가 널일 경우 베이스 예외를 던진다.
      if(publicKey == null) {
        throw new RuntimeException("공개키가 널 임!!");
      }

      // JWT 내의 클래임을 리턴한다.
      String payload = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody().toString();
      //System.out.println("payload = " + payload);

      // Gson은 써 봤지만 안 된다. 페이로드 내부에 // 나 링크 같은 특수한 문자들이 있어서
      // Gson이 거기에서 계속 JSON 문법 예외를 만들어내기 때문이다.
      // Gson을 쓰지 않을 경우, 결국 일일이 수동으로 파싱해서 내용물을 가져오는 수밖에 없다..

      String[] payloadArr = payload.split(", ");
      AppleOAuthLoginPrincipalDto principal = new AppleOAuthLoginPrincipalDto();

      for(String payloadVal : payloadArr){
        String[] payloadKeyValuePair = payloadVal.split("=");
        if(payloadKeyValuePair[0].equals("sub")){
          principal.setSub(payloadKeyValuePair[1]);
        } else if (payloadKeyValuePair[0].equals("email")) {
          principal.setEmail(payloadKeyValuePair[1]);
        }
      }

      return principal;
    } catch (SignatureException | MalformedJwtException e) {
      //토큰 서명 검증 or 구조 문제 (Invalid token)
      //System.out.println("제이더블유티 구조에 문제!!");
      e.printStackTrace();
    } catch (ExpiredJwtException expiredJwtException) {
      //토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
      //System.out.println("토큰 만료됨 예외!!");
      expiredJwtException.printStackTrace();
    } catch (Exception exception) {
      //
      //System.out.println("그냥 전체 예외!!");
      exception.printStackTrace();
    }

    //System.out.println("뭔가 예외 잡힘?!");
    // 앞부분에서 뭔가 예외가 잡혔다면 여기에 의해서 결국 null이 리턴될 것이다.
    return null;
  }
}
