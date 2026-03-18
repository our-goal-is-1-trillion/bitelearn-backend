package com.ogi1t.bitelearn.domain.learning.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQuizAnswer {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private Long chapterId;
  private Long quizId;

  private String selectedAnswer;
  private boolean isCorrect;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @Builder
  public UserQuizAnswer(Long userId, Long chapterId, Long quizId, String selectedAnswer, boolean isCorrect) {
    this.userId = userId;
    this.chapterId = chapterId;
    this.quizId = quizId;
    this.selectedAnswer = selectedAnswer;
    this.isCorrect = isCorrect;
  }
}