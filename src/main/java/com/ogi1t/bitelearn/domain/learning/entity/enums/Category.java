package com.ogi1t.bitelearn.domain.learning.entity.enums;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

  REAL_ESTATE_HOUSING("부동산 · 주거", Arrays.asList(
      Topic.JEONSE,
      Topic.MONTHLY_RENT,
      Topic.BUYING
  )),

  LIVING_FINANCE_EMPLOYMENT("생활금융 · 고용", Arrays.asList(
      Topic.INCOME_EXPENDITURE,
      Topic.CREDIT_LIABILITIES,
      Topic.WORK_WELFARE
  )),

  CAREER_TAX("커리어 · 세무", Arrays.asList(
      Topic.SALARY_REAL_INCOME,
      Topic.INCOME_TAX_DEDUCTION,
      Topic.COMPREHENSIVE_INCOME_TAX
  )),

  ASSET_MANAGEMENT_INVESTMENT("자산운용 · 투자", Arrays.asList(
      Topic.STOCK,
      Topic.BOND_DEPOSIT,
      Topic.ANNUITY
  ));

  private final String description;
  private final List<Topic> topics;
}