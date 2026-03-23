package com.ogi1t.bitelearn.domain.user.dto.response;

import com.ogi1t.bitelearn.domain.auth.entity.enums.ProviderType;
import com.ogi1t.bitelearn.domain.user.entity.User;
import lombok.AllArgsConstructor;
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

  private Integer level;
  private Integer totalBytes;
  private RecentLearningDto recentLearning;

  @Getter
  @Builder
  @AllArgsConstructor
  public static class RecentLearningDto {
    private String categoryCode;  // 예: "REAL_ESTATE_HOUSING"
    private String categoryName;  // 예: "부동산 · 주거"
    private String topicCode;     // 예: "MONTHLY_RENT"
    private String topicName;     // 예: "월세"
    private String chapterTitle;  // 예: "내 소중한 돈을 사수하라! 💸"
    private Long chapterId;       // 예: 3
    private Integer progressRate; // 예: 50 (진행률 %)
  }
}