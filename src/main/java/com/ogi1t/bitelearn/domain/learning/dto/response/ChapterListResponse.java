package com.ogi1t.bitelearn.domain.learning.dto.response;

import com.ogi1t.bitelearn.domain.learning.entity.enums.ProgressStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChapterListResponse {
  private List<ChapterSummaryDto> chapters;

  @Getter
  @AllArgsConstructor
  public static class ChapterSummaryDto {
    private Long chapterId;
    private String title;
    private Integer sequence;
    private ProgressStatus status;
  }
}