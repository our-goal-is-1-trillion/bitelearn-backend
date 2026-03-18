package com.ogi1t.bitelearn.domain.learning.dto.info;

import com.ogi1t.bitelearn.domain.learning.entity.Vocabulary;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VocabInfo {
  private Long id;
  private String frontMain;
  private String frontSub;
  private String frontImageUrl;
  private String backMain;
  private String backSub;

  public static VocabInfo from(Vocabulary v) {
    return new VocabInfo(
        v.getId(),
        v.getFrontMain(),
        v.getFrontSub(),
        v.getFrontImageUrl(),
        v.getBackMain(),
        v.getBackSub()
    );
  }
}