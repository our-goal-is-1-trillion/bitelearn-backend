package com.ogi1t.bitelearn.domain.learning.dto.response;

import com.ogi1t.bitelearn.domain.learning.dto.info.QuizInfo;
import com.ogi1t.bitelearn.domain.learning.dto.info.VocabInfo;
import com.ogi1t.bitelearn.domain.learning.entity.enums.ProgressStatus;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChapterLearningResponse {

  private Long chapterId;
  private Integer chapterSequence;

  private String category;
  private String topic;

  private String chapterTitle;
  private String prologueSubtitle;
  private String prologueContent;
  private String currentGoal;
  private List<String> coreKeywords;

  private ProgressStatus currentStatus;
  private Integer resumeQuizSequence;
  private List<VocabInfo> vocabs;
  private List<QuizInfo> quizzes;

  private String closingMessage;
}