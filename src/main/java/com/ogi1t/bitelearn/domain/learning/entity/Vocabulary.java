package com.ogi1t.bitelearn.domain.learning.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vocabulary {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long chapterId;

  private String frontMain;
  private String frontSub;
  private String frontImageUrl;

  private String backMain;
  private String backSub;
}