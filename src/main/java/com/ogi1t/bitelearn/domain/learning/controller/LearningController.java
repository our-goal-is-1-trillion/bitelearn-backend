package com.ogi1t.bitelearn.domain.learning.controller;

import com.ogi1t.bitelearn.domain.learning.dto.request.QuizSubmitRequest;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterLearningResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterListResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterResultResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.QuizSubmitResponse;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import com.ogi1t.bitelearn.domain.learning.service.LearningService;
import com.ogi1t.bitelearn.global.exception.BusinessException;
import com.ogi1t.bitelearn.global.exception.domain.LearningErrorCode;
import com.ogi1t.bitelearn.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Learning API", description = "학습 API")
@RestController
@RequestMapping("/learning/chapters")
@RequiredArgsConstructor
public class LearningController {

  private final LearningService learningService;

  @Operation(summary = "챕터 목록 조회", description = "카테고리와 주제에 맞는 챕터 목록을 조회합니다. (비회원 접근 가능)")
  @GetMapping
  public ResponseEntity<ChapterListResponse> getChapters(
      @RequestParam Category category,
      @RequestParam Topic topic,
      @AuthenticationPrincipal CustomPrincipal principal) {

    // 비회원(null) 분기 처리
    Long userId = (principal != null) ? principal.getUserId() : null;

    ChapterListResponse response = learningService.getChaptersByCategoryAndTopic(userId, category, topic);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "단일 챕터 학습 데이터 조회", description = "챕터 진입 시 단어장과 퀴즈(정답 제외) 데이터를 조회합니다.")
  @GetMapping("/{chapterId}")
  public ResponseEntity<ChapterLearningResponse> getChapterLearningData(
      @PathVariable Long chapterId,
      @AuthenticationPrincipal CustomPrincipal principal) {

    // 비회원 접근 차단
    if (principal == null) {
      throw new BusinessException(LearningErrorCode.UNAUTHORIZED_ACCESS);
    }

    ChapterLearningResponse response = learningService.getChapterLearningData(principal.getUserId(), chapterId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "단어장 완료 처리", description = "단어장 학습을 마치고 퀴즈로 넘어갈 때 진행 상태를 업데이트합니다.")
  @PostMapping("/{chapterId}/vocab-complete")
  public ResponseEntity<Void> completeVocabulary(
      @PathVariable Long chapterId,
      @AuthenticationPrincipal CustomPrincipal principal) {

    // 비회원 접근 차단
    if (principal == null) {
      throw new BusinessException(LearningErrorCode.UNAUTHORIZED_ACCESS);
    }

    learningService.completeVocabulary(principal.getUserId(), chapterId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "퀴즈 정답 제출 및 채점", description = "한 문제의 정답을 제출하고 채점 결과를 받습니다.")
  @PostMapping("/{chapterId}/quizzes/{quizId}")
  public ResponseEntity<QuizSubmitResponse> submitQuiz(
      @PathVariable Long chapterId,
      @PathVariable Long quizId,
      @RequestBody QuizSubmitRequest request,
      @AuthenticationPrincipal CustomPrincipal principal) {

    // 비회원 접근 차단
    if (principal == null) {
      throw new BusinessException(LearningErrorCode.UNAUTHORIZED_ACCESS);
    }

    QuizSubmitResponse response = learningService.submitQuizAnswer(principal.getUserId(), chapterId, quizId, request);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "챕터 최종 결과 조회", description = "챕터 학습을 마친 후 정답률과 보상 결과를 조회합니다.")
  @GetMapping("/{chapterId}/result")
  public ResponseEntity<ChapterResultResponse> getChapterResult(
      @PathVariable Long chapterId,
      @AuthenticationPrincipal CustomPrincipal principal) {

    // 비회원 접근 차단
    if (principal == null) {
      throw new BusinessException(LearningErrorCode.UNAUTHORIZED_ACCESS);
    }

    ChapterResultResponse response = learningService.getChapterResult(principal.getUserId(), chapterId);
    return ResponseEntity.ok(response);
  }
}