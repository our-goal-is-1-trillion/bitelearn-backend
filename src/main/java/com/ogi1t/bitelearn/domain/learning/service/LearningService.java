package com.ogi1t.bitelearn.domain.learning.service;

import com.ogi1t.bitelearn.domain.learning.dto.request.QuizSubmitRequest;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterLearningResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterListResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.ChapterResultResponse;
import com.ogi1t.bitelearn.domain.learning.dto.response.QuizSubmitResponse;
import com.ogi1t.bitelearn.domain.learning.dto.info.QuizInfo;
import com.ogi1t.bitelearn.domain.learning.dto.info.VocabInfo;
import com.ogi1t.bitelearn.domain.learning.entity.Chapter;
import com.ogi1t.bitelearn.domain.learning.entity.LearningProgress;
import com.ogi1t.bitelearn.domain.learning.entity.Quiz;
import com.ogi1t.bitelearn.domain.learning.entity.UserQuizAnswer;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.entity.enums.ProgressStatus;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import com.ogi1t.bitelearn.domain.learning.repository.ChapterRepository;
import com.ogi1t.bitelearn.domain.learning.repository.LearningProgressRepository;
import com.ogi1t.bitelearn.domain.learning.repository.QuizRepository;
import com.ogi1t.bitelearn.domain.learning.repository.UserQuizAnswerRepository;
import com.ogi1t.bitelearn.domain.learning.repository.VocabularyRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningService {

  private final ChapterRepository chapterRepository;
  private final VocabularyRepository vocabularyRepository;
  private final QuizRepository quizRepository;
  private final LearningProgressRepository progressRepository;
  private final UserQuizAnswerRepository answerRepository;

  // 1. 챕터 목록 조회
  public ChapterListResponse getChaptersByCategoryAndTopic(Long userId, Category category, Topic topic) {
    // 해당 카테고리/토픽의 챕터 목록 가져오기
    List<Chapter> chapters = chapterRepository.findByCategoryAndTopicOrderBySequenceAsc(category, topic);

    // 유저의 진행도 맵으로 만들기 (매핑 속도 최적화)
    List<Long> chapterIds = chapters.stream().map(Chapter::getId).collect(Collectors.toList());
    Map<Long, ProgressStatus> progressMap = progressRepository.findByUserIdAndChapterIdIn(userId, chapterIds)
        .stream()
        .collect(Collectors.toMap(LearningProgress::getChapterId, LearningProgress::getStatus));

    // DTO로 변환 (진행도 정보가 없으면 기본값 READY 부여)
    List<ChapterListResponse.ChapterSummaryDto> chapterDtos = chapters.stream()
        .map(chapter -> new ChapterListResponse.ChapterSummaryDto(
            chapter.getId(),
            chapter.getTitle(),
            chapter.getSequence(),
            progressMap.getOrDefault(chapter.getId(), ProgressStatus.READY)
        ))
        .collect(Collectors.toList());

    return new ChapterListResponse(chapterDtos);
  }

  // 2. 단일 챕터 학습 데이터 조회 (진행도 자동 생성 포함)
  @Transactional
  public ChapterLearningResponse getChapterLearningData(Long userId, Long chapterId) {
    // 진행도가 없으면 새로 생성 (최초 진입)
    LearningProgress progress = progressRepository.findByUserIdAndChapterId(userId, chapterId)
        .orElseGet(() -> progressRepository.save(new LearningProgress(userId, chapterId)));

    // 단어장 및 퀴즈(정답 제외) 조회
    List<VocabInfo> vocabs = vocabularyRepository.findByChapterId(chapterId).stream()
        .map(VocabInfo::from)
        .collect(Collectors.toList());

    List<QuizInfo> quizzes = quizRepository.findByChapterIdOrderBySequenceAsc(chapterId).stream()
        .map(QuizInfo::withoutAnswer)
        .collect(Collectors.toList());

    return ChapterLearningResponse.builder()
        .currentStatus(progress.getStatus())
        .resumeQuizSequence(progress.getLastSolvedQuizSequence())
        .vocabs(vocabs)
        .quizzes(quizzes)
        .build();
  }

  // 3. 단어장 완료 처리 (상태 변경)
  @Transactional
  public void completeVocabulary(Long userId, Long chapterId) {
    LearningProgress progress = progressRepository.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(() -> new IllegalArgumentException("진행도 정보가 없습니다."));

    // 단어장을 다 봤으므로 퀴즈 진행 상태로 변경
    progress.startQuiz();
  }

  // 4. 퀴즈 제출 및 채점 (자동 저장)
  @Transactional
  public QuizSubmitResponse submitQuizAnswer(Long userId, Long chapterId, Long quizId, QuizSubmitRequest request) {
    Quiz quiz = quizRepository.findById(quizId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

    // 정답 비교
    boolean isCorrect = quiz.getCorrectAnswer().equals(request.getSelectedAnswer());

    // 유저 답안 로깅
    UserQuizAnswer answer = new UserQuizAnswer(userId, chapterId, quiz.getId(), request.getSelectedAnswer(), isCorrect);
    answerRepository.save(answer);

    // 진행도 업데이트 (이탈 시 여기부터 재개)
    LearningProgress progress = progressRepository.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(() -> new IllegalArgumentException("진행도 정보가 없습니다."));

    int totalQuizzes = quizRepository.countByChapterId(chapterId);

    // 마지막 문제인지 확인 후 상태 업데이트
    if (quiz.getSequence() >= totalQuizzes) {
      progress.updateProgress(quiz.getSequence(), ProgressStatus.COMPLETED);
      // TODO: (선택) 보상(바이트) 지급 로직을 여기에 추가할 수 있습니다.
    } else {
      progress.updateProgress(quiz.getSequence(), ProgressStatus.QUIZ_IN_PROGRESS);
    }

    return new QuizSubmitResponse(isCorrect, quiz.getCorrectAnswer(), quiz.getExplanation(), progress.getStatus());
  }

  // 5. 최종 결과 조회
  public ChapterResultResponse getChapterResult(Long userId, Long chapterId) {
    int totalQuizzes = quizRepository.countByChapterId(chapterId);
    int correctQuizzes = answerRepository.countByUserIdAndChapterIdAndIsCorrectTrue(userId, chapterId);

    // 0으로 나누기 방지
    int accuracyRate = totalQuizzes > 0 ? (int) Math.round(((double) correctQuizzes / totalQuizzes) * 100) : 0;

    // 임시 보상 로직 (기획에 따라 변경 가능)
    int earnedBytes = accuracyRate >= 80 ? 500 : 100;

    return ChapterResultResponse.builder()
        .correctCount(correctQuizzes)
        .totalCount(totalQuizzes)
        .accuracyRate(accuracyRate)
        .earnedBytes(earnedBytes)
        .build();
  }
}