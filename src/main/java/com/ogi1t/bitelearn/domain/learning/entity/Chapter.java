package com.ogi1t.bitelearn.domain.learning.entity;

import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chapter {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Category category;

  @Enumerated(EnumType.STRING)
  private Topic topic;

  private String title;

  private String prologueSubtitle; // 프롤로그 소제목

  @Column(columnDefinition = "TEXT")
  private String prologueContent; // 프롤로그 내용

  private String currentGoal; // 이번 목표

  private String coreKeywords; // 핵심 내용 키워드

  private Integer sequence; // 챕터 순서 (1, 2, 3...)

  @Column(columnDefinition = "TEXT")
  private String closingMessage; // 마무리 멘트
}