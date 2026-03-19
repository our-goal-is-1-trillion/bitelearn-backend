package com.ogi1t.bitelearn.domain.note.dto.response;

import com.ogi1t.bitelearn.domain.learning.dto.info.QuizInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IncorrectNoteDetailResponse {
  // 유저 글로벌 정보
  private int totalCount;
  private int totalBytes;

  // 오답 노트 메타 정보
  private Long noteId;
  private Long chapterId;
  private LocalDateTime createdAt;

  // 내가 제출한 오답과 실제 정답/해설
  private String userAnswer;
  private String correctAnswer;
  private String explanation;

  // 학습 API에서 쓰던 QuizInfo 구조를 그대로 재사용
  private QuizInfo quiz;
}