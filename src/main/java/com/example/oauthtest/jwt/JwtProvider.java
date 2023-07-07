package com.example.oauthtest.jwt;

import com.example.oauthtest.enums.JwtTypes;
import com.example.oauthtest.jwt.principal.CommonPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtProvider {

  @Value("${jwt.secret.key}")
  private String SECRET_KEY;

  // 하나의 JWT 필터에서 두 종류의 JWT를 처리하므로, 상황에 따라서 다른 종류의 JWT를 만들어야 한다.
  // jjwt 라이브러리로 하나의 jwt를 만들 때 subject는 한 번만 설정해 줄 수 있다.
  // 그 한 번 안에 두 종류의 jwt 각각이 포함해야하는 정보를 모두 넣어야 한다.

  public String createAdminJWT(int id) {
    CommonPrincipal commonPrincipal = CommonPrincipal.builder()
        .jwtType(JwtTypes.ADMIN.getValue())
        .adminId(id)
        .build();

    return Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .setSubject(commonPrincipal.toString())
        .compact();
  }

  public String createMobileJWT(String iss, String jti, Long iat, Long exp) {
    CommonPrincipal commonPrincipal = CommonPrincipal.builder()
        .jwtType(JwtTypes.MOBILE.getValue())
        .iss(iss)
        .jti(jti)
        .iat(iat)
        .exp(exp)
        .build();

    return Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .setSubject(commonPrincipal.toString())
        .compact();
  }

  public CommonPrincipal validateAndGetPrincipal(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();

    return CommonPrincipal.fromString(claims.getSubject());
  }

}
