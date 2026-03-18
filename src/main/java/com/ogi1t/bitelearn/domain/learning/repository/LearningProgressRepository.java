package com.ogi1t.bitelearn.domain.learning.repository;

import com.ogi1t.bitelearn.domain.learning.entity.LearningProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
  Optional<LearningProgress> findByUserIdAndChapterId(Long userId, Long chapterId);
  List<LearningProgress> findByUserIdAndChapterIdIn(Long userId, List<Long> chapterIds);
}