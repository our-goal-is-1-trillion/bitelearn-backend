package com.ogi1t.bitelearn.domain.learning.repository;

import com.ogi1t.bitelearn.domain.learning.entity.UserQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer, Long> {
  int countByUserIdAndChapterIdAndIsCorrectTrue(Long userId, Long chapterId);
}