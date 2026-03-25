package com.ogi1t.bitelearn.global.exception.domain;

import com.ogi1t.bitelearn.global.exception.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum NoteErrorCode implements ApiCode {

  NOTE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 404, "존재하지 않는 오답 노트 기록입니다."),
  UNAUTHORIZED_NOTE_ACCESS(HttpStatus.FORBIDDEN.value(), 403, "본인의 오답 노트만 조회할 수 있습니다.");

  private final Integer httpStatus;
  private final Integer code;
  private final String message;
}