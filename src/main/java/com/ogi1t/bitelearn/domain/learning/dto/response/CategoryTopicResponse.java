package com.ogi1t.bitelearn.domain.learning.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryTopicResponse {
  private String categoryCode; // 예: "REAL_ESTATE_HOUSING"
  private String categoryName; // 예: "부동산"
  private List<TopicDto> topics;

  @Getter
  @AllArgsConstructor
  public static class TopicDto {
    private String topicCode; // 예: "MONTHLY_RENT"
    private String topicName; // 예: "월세"
  }
}