package com.ogi1t.bitelearn.domain.dashboard.controller;

import com.ogi1t.bitelearn.domain.dashboard.dto.response.RecommendedChapterResponse;
import com.ogi1t.bitelearn.domain.dashboard.service.DashboardService;
import com.ogi1t.bitelearn.global.exception.BusinessException;
import com.ogi1t.bitelearn.global.exception.domain.LearningErrorCode;
import com.ogi1t.bitelearn.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard API", description = "대시보드(홈 화면) API")
@RestController
@RequestMapping("/dashboards")
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;

  @Operation(summary = "랜덤 학습 챕터 추천", description = "홈 화면에 노출될 랜덤 학습 챕터 2개를 추천합니다. 유저가 한 번도 학습하지 않은 완전히 새로운 토픽을 우선적으로 노출하여 학습의 다양성을 제공합니다.")
  @GetMapping("/recommendations")
  public ResponseEntity<List<RecommendedChapterResponse>> getRecommendations(
      @AuthenticationPrincipal CustomPrincipal principal) {

    // 비회원 접근 차단 (유저의 기존 학습 기록을 기반으로 추천해야 하므로 로그인 필수)
    if (principal == null) {
      throw new BusinessException(LearningErrorCode.UNAUTHORIZED_ACCESS);
    }

    List<RecommendedChapterResponse> response = dashboardService.getRandomRecommendations(principal.getUserId());
    return ResponseEntity.ok(response);
  }
}