package com.ogi1t.bitelearn.domain.note.dto.response;

import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IncorrectNoteListResponse {
  private int totalCount;
  private int totalBytes;
  private List<IncorrectNoteDto> notes;

  // 무한 스크롤을 위한 필드 추가
  private Long nextCursor;
  private boolean hasNext;

  @Getter
  @AllArgsConstructor
  public static class IncorrectNoteDto {
    private Long noteId;
    private Long chapterId;
    private Long quizId;
    private Category category;
    private Topic topic;
    private Integer chapterSequence;
    private String questionTitle;
    private String userAnswer;
    private String correctAnswer;
    private LocalDateTime createdAt;
  }
}