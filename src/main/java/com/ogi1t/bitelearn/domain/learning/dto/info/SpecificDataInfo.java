package com.ogi1t.bitelearn.domain.learning.dto.info;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificDataInfo {

  // 데이터가 없어도 null 대신 빈 배열([])이 들어가도록 초기화
  @Builder.Default
  private List<String> options = new ArrayList<>();

  @Builder.Default
  private List<DialogueInfo> dialogues = new ArrayList<>();

  @Builder.Default
  private List<DocumentElementInfo> documentElements = new ArrayList<>();

  // --- 내부 클래스 ---

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DocumentElementInfo {
    private String key;
    private String value;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DialogueInfo {
    private String speaker;
    private String message;
  }
}