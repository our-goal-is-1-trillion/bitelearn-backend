package com.ogi1t.bitelearn.domain.learning.dto.response;

import com.ogi1t.bitelearn.domain.learning.entity.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizSubmitResponse {
  private boolean isCorrect;
  private String correctAnswer;
  private String explanation;
  private ProgressStatus newStatus;
}