package com.ogi1t.bitelearn.domain.learning.service;

import com.ogi1t.bitelearn.domain.learning.dto.request.QuizSubmitRequest;
import com.ogi1t.bitelearn.domain.learning.dto.response.CategoryTopicResponse;
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
import com.ogi1t.bitelearn.domain.user.entity.User;
import com.ogi1t.bitelearn.domain.user.repository.UserRepository;
import com.ogi1t.bitelearn.global.exception.BusinessException;
import com.ogi1t.bitelearn.global.exception.domain.LearningErrorCode;
import com.ogi1t.bitelearn.global.exception.domain.UserErrorCode;
import java.util.Arrays;
import java.util.Collections;
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
  private final UserRepository userRepository;

  /**
   * 전체 카테고리(대분류) 및 토픽(중분류) 목록 조회
   */
  public List<CategoryTopicResponse> getAllCategoriesAndTopics() {
    return Arrays.stream(Category.values())
        .map(category -> {
          List<CategoryTopicResponse.TopicDto> topicDtos = category.getTopics().stream()
              .map(topic -> new CategoryTopicResponse.TopicDto(
                  topic.name(),
                  topic.getDescription()
              ))
              .collect(Collectors.toList());

          return CategoryTopicResponse.builder()
              .categoryCode(category.name())
              .categoryName(category.getDescription())
              .topics(topicDtos)
              .build();
        })
        .collect(Collectors.toList());
  }

  /**
   * 챕터 목록 조회
   */
  public ChapterListResponse getChaptersByCategoryAndTopic(Long userId, Category category, Topic topic) {
    List<Chapter> chapters = chapterRepository.findByCategoryAndTopicOrderBySequenceAsc(category, topic);
    List<Long> chapterIds = chapters.stream().map(Chapter::getId).collect(Collectors.toList());

    Map<Long, ProgressStatus> progressMap;
    if (userId != null) {
      progressMap = progressRepository.findByUserIdAndChapterIdIn(userId, chapterIds)
          .stream()
          .collect(Collectors.toMap(LearningProgress::getChapterId, LearningProgress::getStatus));
    } else {
      progressMap = Collections.emptyMap(); // 비회원은 빈 맵 처리
    }

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

  /**
   * 단일 챕터 학습 데이터 조회 (진행도 자동 생성 포함)
   */
  @Transactional
  public ChapterLearningResponse getChapterLearningData(Long userId, Long chapterId) {
    // 챕터 기본 정보 조회
    Chapter chapter = chapterRepository.findById(chapterId)
        .orElseThrow(() -> new BusinessException(LearningErrorCode.CHAPTER_NOT_FOUND));

    // 진행도가 없으면 새로 생성 (최초 진입) or 유저의 진행도 정보를 DB에서 꺼내옴
    LearningProgress progress = progressRepository.findByUserIdAndChapterId(userId, chapterId)
        .orElseGet(() -> progressRepository.save(new LearningProgress(userId, chapterId)));

    // 단어장 및 퀴즈(정답 제외) 조회
    List<VocabInfo> vocabs = vocabularyRepository.findByChapterId(chapterId).stream()
        .map(VocabInfo::from)
        .collect(Collectors.toList());

    List<QuizInfo> quizzes = quizRepository.findByChapterIdOrderBySequenceAsc(chapterId).stream()
        .map(QuizInfo::withoutAnswer)
        .collect(Collectors.toList());

    // 이어서 풀 문제 번호(resumeQuizSequence) 계산 로직
    Integer nextQuizSequence = (progress.getStatus() == ProgressStatus.READY)
        ? null
        : progress.getLastSolvedQuizSequence() + 1;

    // List 형태로 coreKeywords 를 바꿔줌
    List<String> keywordList = Arrays.stream(chapter.getCoreKeywords().split(","))
        .map(String::trim)
        .collect(Collectors.toList());

    return ChapterLearningResponse.builder()
        .chapterId(chapter.getId())
        .chapterSequence(chapter.getSequence())
        .category(chapter.getCategory().name())
        .topic(chapter.getTopic().name())
        .chapterTitle(chapter.getTitle())
        .prologueSubtitle(chapter.getPrologueSubtitle())
        .prologueContent(chapter.getPrologueContent())
        .currentGoal(chapter.getCurrentGoal())
        .coreKeywords(keywordList)
        .currentStatus(progress.getStatus())
        .resumeQuizSequence(nextQuizSequence) // 저장해둔 마지막 문제 번호 + 1 을 프론트엔드로 내려줌 (이어서 풀 문제 번호)
        .vocabs(vocabs)
        .quizzes(quizzes)
        .closingMessage(chapter.getClosingMessage())
        .build();
  }

  /**
   * 단어장 완료 처리 (상태 변경)
   */
  @Transactional
  public void completeVocabulary(Long userId, Long chapterId) {
    LearningProgress progress = progressRepository.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(() -> new BusinessException(LearningErrorCode.PROGRESS_NOT_FOUND));

    progress.startQuiz();
  }

  /**
   * 퀴즈 제출 및 채점 (자동 저장)
   */
  @Transactional
  public QuizSubmitResponse submitQuizAnswer(Long userId, Long chapterId, Long quizId, QuizSubmitRequest request) {
    // 방어 로직 추가: 답안이 비어있거나 null로 들어왔을 때 튕겨내기
    if (request.getSelectedAnswer() == null || request.getSelectedAnswer().trim().isEmpty()) {
      throw new BusinessException(LearningErrorCode.INVALID_QUIZ_SUBMISSION);
    }

    Quiz quiz = quizRepository.findById(quizId)
        .orElseThrow(() -> new BusinessException(LearningErrorCode.QUIZ_NOT_FOUND));

    // 유저가 보낸 답과 실제 정답 비교 (채점)
    boolean isCorrect = quiz.getCorrectAnswer().equals(request.getSelectedAnswer());

    UserQuizAnswer answer = new UserQuizAnswer(
        userId,
        chapterId,
        quiz.getId(),
        request.getSelectedAnswer(), // 유저가 고른 답
        isCorrect                    // 정답 여부
    );
    answerRepository.save(answer);   // DB(user_quiz_answer 테이블)에 INSERT

    // 유저의 해당 챕터 진행도 정보를 불러옴
    LearningProgress progress = progressRepository.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(() -> new BusinessException(LearningErrorCode.PROGRESS_NOT_FOUND));

    int totalQuizzes = quizRepository.countByChapterId(chapterId);

    // 마지막 문제가 아니면 "진행 중" 상태와 함께 '방금 푼 문제 번호'를 저장함
    if (quiz.getSequence() >= totalQuizzes) {
      progress.updateProgress(quiz.getSequence(), ProgressStatus.COMPLETED);
    } else {
      // 퀴즈를 풀다 나갔다면, DB에는 이 마지막 sequence 번호가 남아있게 됨
      progress.updateProgress(quiz.getSequence(), ProgressStatus.QUIZ_IN_PROGRESS);
    }

    return new QuizSubmitResponse(isCorrect, quiz.getCorrectAnswer(), quiz.getExplanation(), progress.getStatus());
  }

  /**
   * 최종 결과 조회
   */
  @Transactional
  public ChapterResultResponse getChapterResult(Long userId, Long chapterId) {
    int totalQuizzes = quizRepository.countByChapterId(chapterId);
    int correctQuizzes = answerRepository.countLatestCorrectAnswers(userId, chapterId);
    int incorrectQuizzes = totalQuizzes - correctQuizzes;

    int accuracyRate = totalQuizzes > 0 ? (int) Math.round(((double) correctQuizzes / totalQuizzes) * 100) : 0;

    // 유저의 진행도 정보와 회원 정보 가져오기
    LearningProgress progress = progressRepository.findByUserIdAndChapterId(userId, chapterId)
        .orElseThrow(() -> new BusinessException(LearningErrorCode.PROGRESS_NOT_FOUND));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

    final int earnedBytes;
    final int lostBytes;

    if (progress.isRewarded()) {
      // 이미 보상을 받은 경우 둘 다 0 처리 (재대입 없이 여기서 최초 할당)
      earnedBytes = 0;
      lostBytes = 0;
    } else {
      earnedBytes = correctQuizzes * 50;
      lostBytes = incorrectQuizzes * -20;

      user.addBytes(earnedBytes + lostBytes);
      progress.markAsRewarded();
    }

    return ChapterResultResponse.builder()
        .correctCount(correctQuizzes)
        .totalCount(totalQuizzes)
        .accuracyRate(accuracyRate)
        .earnedBytes(earnedBytes)
        .lostBytes(lostBytes)
        .currentLevel(user.getLevel())
        .currentTotalBytes(user.getTotalBytes())
        .build();
  }
}