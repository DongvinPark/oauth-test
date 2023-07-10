package com.example.oauthtest.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestController {

  //private OidcUserService oidcUserService;

  @GetMapping("/")
  public String home(
      @AuthenticationPrincipal OidcUser oidcUser
  ){
    System.out.println("홈 이동 호출!");

    //OidcUser oidcUser = oidcUserService.loadUser(userRequest);
    if (oidcUser == null) {
      System.out.println("oidc 유저 널!!");
    }
    if(oidcUser != null){
      System.out.println("oidc 유저 낫널 진입!!");

      System.out.println("oidc 유저인포 스트링 : " + oidcUser.getUserInfo().toString());
      System.out.println("oidc 유저인포 겟 섭젝트 스트링 : " + oidcUser.getSubject());
      System.out.println("oidc 유저인포 겟 아이디토큰 스트링 : " + oidcUser.getIdToken());

      Map<String, Object> oidcUserClaim = oidcUser.getClaims();
      System.out.println("oidc 유저인포 클래임 정보 순회");
      for(String claimMapKey : oidcUserClaim.keySet()){
        System.out.println("클래임 키 : " + claimMapKey);
        System.out.println("클래임 값 : " + oidcUserClaim.get(claimMapKey).toString());
      }
    }

    return "index";
  }

}
