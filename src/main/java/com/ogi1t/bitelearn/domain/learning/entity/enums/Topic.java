package com.ogi1t.bitelearn.domain.learning.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topic {

  // 부동산 · 주거
  JEONSE("전세"),
  MONTHLY_RENT("월세"),
  BUYING("매매"),

  // 생활금융 · 고용
  INCOME_EXPENDITURE("소득 및 지출"),
  CREDIT_LIABILITIES("신용 및 부채"),
  WORK_WELFARE("근로 및 복지"),

  // 커리어 · 세무
  SALARY_REAL_INCOME("월급과 실수령액"),
  INCOME_TAX_DEDUCTION("소득공제와 세액공제"),
  COMPREHENSIVE_INCOME_TAX("종합소득세"),

  // 자산운용 · 투자
  STOCK("주식"),
  BOND_DEPOSIT("채권 · 예금"),
  ANNUITY("연금");

  private final String description; // 프론트엔드에 보여줄 한글 이름
}