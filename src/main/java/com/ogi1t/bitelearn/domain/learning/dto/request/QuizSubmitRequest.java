package com.ogi1t.bitelearn.domain.learning.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {
  private String selectedAnswer;
}