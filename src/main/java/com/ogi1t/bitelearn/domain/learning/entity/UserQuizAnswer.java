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

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long chapterId;

  @Column(nullable = false)
  private Long quizId;

  @Column(nullable = false, length = 500)
  private String selectedAnswer;

  @Column(nullable = false)
  private boolean isCorrect;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
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