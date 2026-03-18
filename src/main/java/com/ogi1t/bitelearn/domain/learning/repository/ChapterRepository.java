package com.ogi1t.bitelearn.domain.learning.repository;

import com.ogi1t.bitelearn.domain.learning.entity.Chapter;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
  List<Chapter> findByCategoryAndTopicOrderBySequenceAsc(Category category, Topic topic);
}