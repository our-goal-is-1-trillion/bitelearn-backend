package com.ogi1t.bitelearn.domain.learning.repository;

import com.ogi1t.bitelearn.domain.learning.entity.Quiz;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  List<Quiz> findByChapterIdOrderBySequenceAsc(Long chapterId);
  int countByChapterId(Long chapterId);
}