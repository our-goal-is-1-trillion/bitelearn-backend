package com.ogi1t.bitelearn.global.exception.domain;

import com.ogi1t.bitelearn.global.exception.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LearningErrorCode implements ApiCode {

  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED.value(), 401, "로그인이 필요한 학습 서비스입니다."),
  CHAPTER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 404, "존재하지 않는 챕터입니다."),
  QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 404, "존재하지 않는 퀴즈입니다."),
  PROGRESS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 404, "학습 진행도 정보를 찾을 수 없습니다."),
  INVALID_QUIZ_SUBMISSION(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 퀴즈 제출 요청입니다.");

  private final Integer httpStatus;
  private final Integer code;
  private final String message;
}