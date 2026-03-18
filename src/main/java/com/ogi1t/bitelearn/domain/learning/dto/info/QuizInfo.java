package com.ogi1t.bitelearn.domain.learning.dto.info;

import com.ogi1t.bitelearn.domain.learning.entity.Quiz;
import com.ogi1t.bitelearn.domain.learning.entity.enums.QuizType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizInfo {
  private Long quizId;
  private Integer sequence;
  private QuizType type;
  private String passageTitle;
  private String passageContent;
  private String questionImageUrl;
  private String questionTitle;
  private Map<String, Object> specificData;

  public static QuizInfo withoutAnswer(Quiz q) {
    return new QuizInfo(
        q.getId(),
        q.getSequence(),
        q.getQuizType(),
        q.getPassageTitle(),
        q.getPassageContent(),
        q.getQuestionImageUrl(),
        q.getQuestionTitle(),
        q.getSpecificData()
    );
  }
}