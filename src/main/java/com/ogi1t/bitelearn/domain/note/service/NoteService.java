package com.ogi1t.bitelearn.domain.note.service;

import com.ogi1t.bitelearn.domain.learning.dto.info.QuizInfo;
import com.ogi1t.bitelearn.domain.learning.entity.Quiz;
import com.ogi1t.bitelearn.domain.learning.entity.UserQuizAnswer;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.repository.QuizRepository;
import com.ogi1t.bitelearn.domain.learning.repository.UserQuizAnswerRepository;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteDetailResponse;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse.IncorrectNoteDto;
import com.ogi1t.bitelearn.global.exception.BusinessException;
import com.ogi1t.bitelearn.global.exception.domain.LearningErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

  private final UserQuizAnswerRepository answerRepository;
  private final QuizRepository quizRepository;
  // private final UserRepository userRepository; // 추후 보유 바이트 연동 시 주입

  // 1. 오답 노트 목록 조회
  public IncorrectNoteListResponse getIncorrectNoteList(Long userId, Category category) {
    // 총 오답 개수
    int totalCount = answerRepository.countByUserIdAndIsCorrectFalse(userId);
    // TODO: 유저 보유 바이트 연동 로직 (임시 1250 바이트)
    int totalBytes = 1250;

    List<IncorrectNoteDto> notes;
    if (category == null) {
      // 카테고리가 없으면 전체 오답 노트 최신순 조회
      notes = answerRepository.findAllIncorrectNotesByUserId(userId);
    } else {
      // 카테고리가 있으면 해당 카테고리만 필터링해서 조회
      notes = answerRepository.findIncorrectNotesByUserIdAndCategory(userId, category);
    }

    return IncorrectNoteListResponse.builder()
        .totalCount(totalCount)
        .totalBytes(totalBytes)
        .notes(notes)
        .build();
  }

  // 2. 오답 노트 상세 조회
  public IncorrectNoteDetailResponse getIncorrectNoteDetail(Long userId, Long noteId) {
    // 오답 기록 조회
    UserQuizAnswer note = answerRepository.findById(noteId)
        .orElseThrow(() -> new BusinessException(LearningErrorCode.NOTE_NOT_FOUND));

    // 권한 검증 (내 오답 노트가 맞는지)
    if (!note.getUserId().equals(userId)) {
      throw new BusinessException(LearningErrorCode.UNAUTHORIZED_ACCESS);
    }

    // 퀴즈 원본 데이터 조회
    Quiz quiz = quizRepository.findById(note.getQuizId())
        .orElseThrow(() -> new BusinessException(LearningErrorCode.QUIZ_NOT_FOUND));

    // 총 오답 개수 및 보유 바이트 (상세 화면 상단에도 그려줘야 하므로)
    int totalCount = answerRepository.countByUserIdAndIsCorrectFalse(userId);
    int totalBytes = 1250;

    // 기존 Learning 도메인의 QuizInfo 재사용 (정답 제외 데이터 세팅)
    QuizInfo reusableQuizInfo = QuizInfo.withoutAnswer(quiz);

    return IncorrectNoteDetailResponse.builder()
        .totalCount(totalCount)
        .totalBytes(totalBytes)
        .noteId(note.getId())
        .chapterId(note.getChapterId())
        .createdAt(note.getCreatedAt())
        .userAnswer(note.getSelectedAnswer())
        .correctAnswer(quiz.getCorrectAnswer())
        .explanation(quiz.getExplanation())
        .quiz(reusableQuizInfo) // 프론트엔드가 그대로 재사용할 퀴즈 컴포넌트 데이터!
        .build();
  }
}