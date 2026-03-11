package com.ogi1t.bitelearn.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Access / Refresh Token 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

  private String grantType;    // 보통 "Bearer"
  private String accessToken;  // JWT Access Token

  @JsonIgnore // 응답 바디(JSON) 직렬화 시 이 필드를 제외시킨다!
  private String refreshToken; // JWT Refresh Token

  private Long accessTokenExpiresIn; // 액세스 토큰 만료 시간 (밀리초)
}