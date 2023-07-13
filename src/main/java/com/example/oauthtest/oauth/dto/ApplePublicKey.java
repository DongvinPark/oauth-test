package com.example.oauthtest.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplePublicKey {
  private String kty;
  private String kid;
  private String use;
  private String alg;
  private String n;
  private String e;
}
