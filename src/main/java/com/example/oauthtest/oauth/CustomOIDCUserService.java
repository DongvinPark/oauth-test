package com.example.oauthtest.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOIDCUserService extends OidcUserService {

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    System.out.println("오아이디씨 유저 서비스 로드유저 메서드 호출!!");

    System.out.println("리다이렉트 uri : " + userRequest.getClientRegistration().getRedirectUri());

    OidcUser oidcUser = super.loadUser(userRequest);

    System.out.println("슈퍼 로드유저 완료!");

    /*GoogleOidcPrincipal oidcPrincipal = new GoogleOidcPrincipal(
        oidcUser.getSubject(), oidcUser.getEmail()
    );*/

    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        oidcUser,
        null,
        AuthorityUtils.NO_AUTHORITIES
    );

    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

    System.out.println("시큐리티 컨텍스트 등록 완료!");

    if(oidcUser != null){
      System.out.println("oidc 유저 낫널 진입!!");

      /*if(oidcUser.getUserInfo() != null){
        System.out.println("oidc 유저인포 스트링 : " + oidcUser.getUserInfo().toString());
      }*/

      /*if(oidcUser.getSubject() != null){
        System.out.println("oidc 유저인포 겟 섭젝트 스트링 : " + oidcUser.getSubject());
      }*/

      if(oidcUser.getIdToken() != null){
        System.out.println("oidc 유저인포 겟 아이디토큰 스트링 : " + oidcUser.getIdToken().getTokenValue());
        System.out.println("oidc 겟 섭젝트 form idtoken : " + oidcUser.getIdToken().getSubject());
        System.out.println("oidc 겟 유저이메일 from idtoken : " + oidcUser.getIdToken().getEmail());
      }

      /*if(oidcUser.getClaims() != null){
        Map<String, Object> oidcUserClaim = oidcUser.getClaims();
        System.out.println("oidc 유저인포 클래임 정보 순회");
        for(String claimMapKey : oidcUserClaim.keySet()){
          System.out.println("클래임 키 : " + claimMapKey);
          System.out.println("클래임 값 : " + oidcUserClaim.get(claimMapKey).toString());
        }
      }*/

    }

    return oidcUser;
  }

}
