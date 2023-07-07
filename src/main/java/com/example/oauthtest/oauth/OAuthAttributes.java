package com.example.oauthtest.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

  private Map<String, Object> attributes;
  private String registrationId;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String picture;

  @Builder
  public OAuthAttributes(Map<String, Object> attributes, String registrationId, String nameAttributeKey, String name,
      String email, String picture) {
    this.registrationId = registrationId;
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }

  public static OAuthAttributes of(
      String registrationId, String userNameAttributeName, Map<String, Object> attributes
  ){
    return OAuthAttributes.builder()
        .registrationId(registrationId)
        .name((String) attributes.get("name"))
        .email((String) attributes.get("email"))
        .picture((String) attributes.get("picture"))
        .attributes(attributes)
        .nameAttributeKey(userNameAttributeName)
        .build();
  }

}
