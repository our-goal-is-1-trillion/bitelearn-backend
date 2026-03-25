package com.ogi1t.bitelearn.global.exception.domain;

import com.ogi1t.bitelearn.global.exception.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum DashboardErrorCode implements ApiCode {

  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED.value(), 401, "대시보드 추천 학습을 보려면 로그인이 필요합니다.");

  private final Integer httpStatus;
  private final Integer code;
  private final String message;
}