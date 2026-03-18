package com.ogi1t.bitelearn.domain.learning.entity;

import com.ogi1t.bitelearn.domain.learning.dto.info.SpecificDataInfo;
import com.ogi1t.bitelearn.domain.learning.entity.enums.QuizType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long chapterId;

  private Integer sequence; // 1 ~ 12

  @Enumerated(EnumType.STRING)
  private QuizType quizType;

  private String passageTitle;

  @Column(columnDefinition = "TEXT")
  private String passageContent;

  private String questionImageUrl;
  private String questionTitle;

  // DB의 JSON 데이터를 자바 객체로 변환
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "json")
  private SpecificDataInfo specificData;

  private String correctAnswer;

  @Column(columnDefinition = "TEXT")
  private String explanation;
}