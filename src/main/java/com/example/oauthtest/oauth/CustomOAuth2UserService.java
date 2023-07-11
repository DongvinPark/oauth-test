package com.example.oauthtest.oauth;

import com.example.oauthtest.model.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    System.out.println("오오스 투 유저 서비스 로드유저 메서드 호출!!");

    /*OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    System.out.println("registrationId = " + registrationId);

    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();
    System.out.println("userNameAttributeName = " + userNameAttributeName);

    OAuthAttributes attributes = OAuthAttributes.of(
        registrationId, userNameAttributeName, oAuth2User.getAttributes()
    );

    // 유저 엔티티 저장은 여기서 했다고 치자. 그냥 로그인일 수도 있고, 회원가입 및 로그인을 돌 다 진행할 수도 있다.
    // 이건 아마 내부 로직을 if else로 나눠서 구현해야 할 것이다.
    System.out.println("로그인만 하든지, 회원가입까지 하든지 둘 중 하나는 했다고 치자.");*/

    /*return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("USER")),
        attributes.getAttributes(),
        attributes.getNameAttributeKey()
    );*/
    return null;
  }

}

