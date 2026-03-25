package com.ogi1t.bitelearn.domain.learning.repository;

import com.ogi1t.bitelearn.domain.learning.entity.Vocabulary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
  List<Vocabulary> findByChapterId(Long chapterId);
}