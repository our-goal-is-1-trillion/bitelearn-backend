package com.ogi1t.bitelearn.domain.auth.dto.request;

import com.ogi1t.bitelearn.domain.auth.entity.enums.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthLoginRequest {

  private OAuthProvider provider; // NAVER, GOOGLE
  private String authorizationCode; // OAuth 인가 코드
}