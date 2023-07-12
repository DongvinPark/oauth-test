package com.example.oauthtest.oauth.apple;

import com.example.oauthtest.oauth.dto.ApplePublicKeyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleJwtUtils {

  public Claims getClaimsBy(String identityToken) {

    try {
      // 애플의 아이디 서버로부터 공개키를 받아온다.
      // 애플이 준 공개키 값을 이용해서 ApplePublicKeyResponse를 구현하는 일은 결국 내가 해야 한다.
      ApplePublicKeyResponse response = null;

      // 아이디토큰으로부터 헤더 부분만을 잘라낸 후, 헤더값을 통해서 애플의 공개키 응답 중 무엇을
      // 아이디토큰 디코드용 공개키를 만드는 것에 사용할 것인지를 결정한다.
      String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
      Map<String, String> header = new ObjectMapper().readValue(
          new String(
              Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"
          ), Map.class
      );
      ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(
          header.get("kid"), header.get("alg")
      ).orElseThrow(
          () -> new NullPointerException("Failed get public key from apple's id server.")
      );

      // 공개키를 만든다.
      byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
      byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());
      BigInteger n = new BigInteger(1, nBytes);
      BigInteger e = new BigInteger(1, eBytes);
      RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
      KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
      PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

      // 만들어진 공개키가 널일 경우 베이스 예외를 던진다.

      // JWT 내의 클래임을 리턴한다.
      return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();

    } catch (MalformedJwtException e) {
      //토큰 서명 검증 or 구조 문제 (Invalid token)
    } catch (ExpiredJwtException expiredJwtException) {
      //토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
    } catch (Exception exception) {
      //
    }

    // 앞부분에서 뭔가 예외가 잡혔다면 여기에 의해서 결국 null이 리턴될 것이다.
    return null;
  }
}
