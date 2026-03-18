package com.ogi1t.bitelearn.domain.learning.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChapterResultResponse {
  private int correctCount;
  private int totalCount;
  private int accuracyRate;
  private int earnedBytes;
}