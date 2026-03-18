package com.ogi1t.bitelearn.domain.learning.dto.info;

import com.ogi1t.bitelearn.domain.learning.entity.Quiz;
import com.ogi1t.bitelearn.domain.learning.entity.enums.QuizType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizInfo {
  private Long quizId;
  private Integer sequence;
  private QuizType type;
  private String passageTitle;
  private String passageContent;
  private String questionImageUrl;
  private String questionTitle;
  private SpecificDataInfo specificData;

  public static QuizInfo withoutAnswer(Quiz quiz) {
    // 안전장치: DB에 특정 퀴즈의 JSON 데이터가 아예 없더라도 뻗지 않고,
    // 빈 배열 3개가 들어있는 껍데기를 만들어서 프론트로 넘겨줌
    SpecificDataInfo safeSpecificData = (quiz.getSpecificData() != null)
        ? quiz.getSpecificData()
        : SpecificDataInfo.builder().build();

    return QuizInfo.builder()
        .quizId(quiz.getId())
        .sequence(quiz.getSequence())
        .type(quiz.getQuizType())
        .passageTitle(quiz.getPassageTitle())
        .passageContent(quiz.getPassageContent())
        .questionImageUrl(quiz.getQuestionImageUrl())
        .questionTitle(quiz.getQuestionTitle())
        .specificData(safeSpecificData) // 안전장치가 적용된 객체 주입
        .build();
  }
}