package com.ogi1t.bitelearn.domain.dashboard.dto.response;

import com.ogi1t.bitelearn.domain.learning.entity.Chapter;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendedChapterResponse {
  private Long chapterId;
  private Category category;
  private Topic topic;
  private String title;
  private String prologueSubtitle;
  private Integer sequence;

  public static RecommendedChapterResponse from(Chapter chapter) {
    return RecommendedChapterResponse.builder()
        .chapterId(chapter.getId())
        .category(chapter.getCategory())
        .topic(chapter.getTopic())
        .title(chapter.getTitle())
        .prologueSubtitle(chapter.getPrologueSubtitle())
        .sequence(chapter.getSequence())
        .build();
  }
}