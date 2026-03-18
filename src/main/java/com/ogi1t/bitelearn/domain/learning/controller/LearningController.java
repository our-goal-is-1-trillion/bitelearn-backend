package com.ogi1t.bitelearn.domain.learning.controller;

import com.ogi1t.bitelearn.domain.learning.dto.request.QuizSubmitRequest;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterLearningResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterListResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterResultResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.QuizSubmitResponse;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import com.ogi1t.bitelearn.domain.learning.service.LearningService;
import com.ogi1t.bitelearn.global.security.CustomPrincipal;
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

  // 1. 챕터 목록 조회 (Query Parameter 방식)
  @GetMapping
  public ResponseEntity<ChapterListResponse> getChapters(
      @RequestParam Category category,
      @RequestParam Topic topic,
      @AuthenticationPrincipal CustomPrincipal principal) {

    ChapterListResponse response = learningService.getChaptersByCategoryAndTopic(principal.getUserId(), category, topic);
    return ResponseEntity.ok(response);
  }

  // 2. 단일 챕터 학습 데이터(단어+퀴즈) 조회
  @GetMapping("/{chapterId}")
  public ResponseEntity<ChapterLearningResponse> getChapterLearningData(
      @PathVariable Long chapterId,
      @AuthenticationPrincipal CustomPrincipal principal) {

    ChapterLearningResponse response = learningService.getChapterLearningData(principal.getUserId(), chapterId);
    return ResponseEntity.ok(response);
  }

  // 3. 단어장 완료 처리
  @PostMapping("/{chapterId}/vocab-complete")
  public ResponseEntity<Void> completeVocabulary(
      @PathVariable Long chapterId,
      @AuthenticationPrincipal CustomPrincipal principal) {

    learningService.completeVocabulary(principal.getUserId(), chapterId);
    return ResponseEntity.ok().build();
  }

  // 4. 퀴즈 1문제 제출 및 채점
  @PostMapping("/{chapterId}/quizzes/{quizId}")
  public ResponseEntity<QuizSubmitResponse> submitQuiz(
      @PathVariable Long chapterId,
      @PathVariable Long quizId,
      @RequestBody QuizSubmitRequest request,
      @AuthenticationPrincipal CustomPrincipal principal) {

    QuizSubmitResponse response = learningService.submitQuizAnswer(principal.getUserId(), chapterId, quizId, request);
    return ResponseEntity.ok(response);
  }

  // 5. 챕터 최종 결과 조회
  @GetMapping("/{chapterId}/result")
  public ResponseEntity<ChapterResultResponse> getChapterResult(
      @PathVariable Long chapterId,
      @AuthenticationPrincipal CustomPrincipal principal) {

    ChapterResultResponse response = learningService.getChapterResult(principal.getUserId(), chapterId);
    return ResponseEntity.ok(response);
  }
}