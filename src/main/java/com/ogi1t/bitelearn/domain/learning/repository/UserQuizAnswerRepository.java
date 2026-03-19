package com.ogi1t.bitelearn.domain.learning.repository;

import com.ogi1t.bitelearn.domain.learning.entity.UserQuizAnswer;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse.IncorrectNoteDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer, Long> {

  // 챕터 결과 채점용 메서드
  int countByUserIdAndChapterIdAndIsCorrectTrue(Long userId, Long chapterId);

  // 전체 오답 노트 조회 (카테고리 조건 없을 때)
  @Query("SELECT new com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse$IncorrectNoteDto(" +
      "uqa.id, c.id, q.id, c.category, c.topic, q.questionTitle, uqa.selectedAnswer, q.correctAnswer, uqa.createdAt) " +
      "FROM UserQuizAnswer uqa " +
      "JOIN Quiz q ON uqa.quizId = q.id " +
      "JOIN Chapter c ON uqa.chapterId = c.id " +
      "WHERE uqa.userId = :userId AND uqa.isCorrect = false " +
      "ORDER BY uqa.createdAt DESC")
  List<IncorrectNoteDto> findAllIncorrectNotesByUserId(@Param("userId") Long userId);

  // 특정 카테고리의 오답 노트 조회 (예: 부동산만 볼 때)
  @Query("SELECT new com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse$IncorrectNoteDto(" +
      "uqa.id, c.id, q.id, c.category, c.topic, q.questionTitle, uqa.selectedAnswer, q.correctAnswer, uqa.createdAt) " +
      "FROM UserQuizAnswer uqa " +
      "JOIN Quiz q ON uqa.quizId = q.id " +
      "JOIN Chapter c ON uqa.chapterId = c.id " +
      "WHERE uqa.userId = :userId AND uqa.isCorrect = false AND c.category = :category " +
      "ORDER BY uqa.createdAt DESC")
  List<IncorrectNoteDto> findIncorrectNotesByUserIdAndCategory(@Param("userId") Long userId, @Param("category") Category category);

  // 총 오답 개수 카운트
  int countByUserIdAndIsCorrectFalse(Long userId);
}