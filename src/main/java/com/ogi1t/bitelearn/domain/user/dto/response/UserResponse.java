package com.ogi1t.bitelearn.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ogi1t.bitelearn.domain.auth.entity.enums.ProviderType;
import com.ogi1t.bitelearn.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

  private Long id;
  private String email;
  private String nickname;
  private ProviderType providerType;
  private Boolean isOnboardingCompleted;

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .providerType(user.getProviderType())
        .isOnboardingCompleted(user.isOnboardingCompleted())
        .build();
  }
}