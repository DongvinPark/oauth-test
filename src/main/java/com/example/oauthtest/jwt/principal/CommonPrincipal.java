package com.example.oauthtest.jwt.principal;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonPrincipal {

  // admin OR mobile
  private String jwtType;

  // 어드민 용 필드
  private Integer adminId;

  // 모바일 용 필드
  private String iss; // 발급자 이름(== 앱 이름. google 등을 것으로 예상.)
  private String jti; // 유저 엔티티의 PK id 컬럼 값
  private Long iat; // 토큰 발행 시각
  private Long exp; // 토큰 만료 시각

  // 빌더 패턴으로 jwt를 만들 때 setSubject()는 마지막에 호출한 것만 jwt에 반영되고,
  //setSubject()는 String 타입만 인자로 받기 때문에 toString()을 오버라이드함.
  @Override
  public String toString() {
    return "jwtType=" + this.jwtType + ","
        + "adminId=" + this.adminId + ","
        //+ "adminUsername=" + this.adminUsername + ","
        + "iss=" + this.iss + ","
        + "jti=" + this.jti + ","
        + "iat=" + this.iat + ","
        + "exp=" + this.exp;
  }

  public static CommonPrincipal fromString(String claim) {

    List<String> claimList = Arrays.stream(claim.split(","))
        .map(s -> s.split("=")[1]).toList();

    return CommonPrincipal.builder()
        .jwtType(claimList.get(0))
        .adminId(claimList.get(1).equals("null") ? null : Integer.parseInt(claimList.get(1)))
        //.adminUsername(claimList.get(2))
        .iss(claimList.get(2))
        .jti(claimList.get(3))
        .iat(claimList.get(4).equals("null") ? null : Long.parseLong(claimList.get(4)))
        .exp(claimList.get(5).equals("null") ? null : Long.parseLong(claimList.get(5)))
        .build();
  }
}
