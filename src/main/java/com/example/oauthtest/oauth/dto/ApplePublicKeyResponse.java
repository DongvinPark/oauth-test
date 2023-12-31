package com.example.oauthtest.oauth.dto;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplePublicKeyResponse {
  private List<ApplePublicKey> keys;

  public Optional<ApplePublicKey> getMatchedKeyBy(String kid, String alg) {
    return this.keys.stream()
        .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
        .findFirst();
  }
}
