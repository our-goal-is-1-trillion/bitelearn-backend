package com.ogi1t.bitelearn.domain.learning.entity;

import com.ogi1t.bitelearn.domain.learning.entity.enums.ProgressStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningProgress {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private Long chapterId;

  @Enumerated(EnumType.STRING)
  private ProgressStatus status;

  private Integer lastSolvedQuizSequence;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Builder
  public LearningProgress(Long userId, Long chapterId) {
    this.userId = userId;
    this.chapterId = chapterId;
    this.status = ProgressStatus.READY;
    this.lastSolvedQuizSequence = 0;
  }

  public void updateProgress(Integer currentQuizSequence, ProgressStatus newStatus) {
    this.lastSolvedQuizSequence = currentQuizSequence;
    this.status = newStatus;
  }

  public void startQuiz() {
    if (this.status == ProgressStatus.READY) {
      this.status = ProgressStatus.QUIZ_IN_PROGRESS;
    }
  }
}