package com.ogi1t.bitelearn.domain.user.dto.response;

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

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .providerType(user.getProviderType())
        .build();
  }
}