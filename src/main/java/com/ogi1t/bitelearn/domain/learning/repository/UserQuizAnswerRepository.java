package com.ogi1t.bitelearn.domain.learning.repository;

import com.ogi1t.bitelearn.domain.learning.entity.UserQuizAnswer;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse.IncorrectNoteDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer, Long> {

  // 챕터 결과 조회용 (재시험 누적 방지): 문제별 최신 풀이 기록 중 정답(isCorrect=true) 개수 조회
  @Query("SELECT COUNT(u) FROM UserQuizAnswer u " +
      "WHERE u.id IN (" +
      "  SELECT MAX(u2.id) FROM UserQuizAnswer u2 " +
      "  WHERE u2.userId = :userId AND u2.chapterId = :chapterId " +
      "  GROUP BY u2.quizId" +
      ") AND u.isCorrect = true")
  int countLatestCorrectAnswers(@Param("userId") Long userId, @Param("chapterId") Long chapterId);

  // 전체 오답 노트 무한 스크롤 조회 (카테고리 조건 없을 때) (uqa.id < :cursor 조건 추가, Pageable 추가)
  @Query("SELECT new com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse$IncorrectNoteDto(" +
      "uqa.id, c.id, q.id, c.category, c.topic, c.sequence, q.questionTitle, uqa.selectedAnswer, q.correctAnswer, uqa.createdAt) " + // ✨ c.sequence 위치 변경!
      "FROM UserQuizAnswer uqa " +
      "JOIN Quiz q ON uqa.quizId = q.id " +
      "JOIN Chapter c ON uqa.chapterId = c.id " +
      "WHERE uqa.userId = :userId AND uqa.isCorrect = false AND uqa.id < :cursor " +
      "ORDER BY uqa.id DESC")
  List<IncorrectNoteDto> findAllIncorrectNotesByCursor(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);

  // 특정 카테고리의 오답 노트 무한 스크롤 조회 (예: 부동산만 볼 때)
  @Query("SELECT new com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse$IncorrectNoteDto(" +
      "uqa.id, c.id, q.id, c.category, c.topic, c.sequence, q.questionTitle, uqa.selectedAnswer, q.correctAnswer, uqa.createdAt) " + // ✨ c.sequence 위치 변경!
      "FROM UserQuizAnswer uqa " +
      "JOIN Quiz q ON uqa.quizId = q.id " +
      "JOIN Chapter c ON uqa.chapterId = c.id " +
      "WHERE uqa.userId = :userId AND uqa.isCorrect = false AND c.category = :category AND uqa.id < :cursor " +
      "ORDER BY uqa.id DESC")
  List<IncorrectNoteDto> findIncorrectNotesByCategoryAndCursor(@Param("userId") Long userId, @Param("category") Category category, @Param("cursor") Long cursor, Pageable pageable);

  // 총 오답 개수 카운트
  int countByUserIdAndIsCorrectFalse(Long userId);
}