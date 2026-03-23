package com.ogi1t.bitelearn.domain.note.service;

import com.ogi1t.bitelearn.domain.learning.dto.info.QuizInfo;
import com.ogi1t.bitelearn.domain.learning.entity.Quiz;
import com.ogi1t.bitelearn.domain.learning.entity.UserQuizAnswer;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.learning.repository.ChapterRepository;
import com.ogi1t.bitelearn.domain.learning.repository.QuizRepository;
import com.ogi1t.bitelearn.domain.learning.repository.UserQuizAnswerRepository;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteDetailResponse;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse.IncorrectNoteDto;
import com.ogi1t.bitelearn.domain.user.entity.User;
import com.ogi1t.bitelearn.domain.user.repository.UserRepository;
import com.ogi1t.bitelearn.global.exception.BusinessException;
import com.ogi1t.bitelearn.global.exception.domain.LearningErrorCode;
import com.ogi1t.bitelearn.global.exception.domain.UserErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

  private final UserQuizAnswerRepository answerRepository;
  private final QuizRepository quizRepository;
  private final ChapterRepository chapterRepository;
  private final UserRepository userRepository;

  // 1. 오답 노트 목록 조회 (무한 스크롤)
  public IncorrectNoteListResponse getIncorrectNoteList(Long userId, Category category, Long cursor) {
    // 총 오답 개수
    int totalCount = answerRepository.countByUserIdAndIsCorrectFalse(userId);

    // 실제 유저 정보 조회 및 보유 바이트 연동
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    int totalBytes = user.getTotalBytes();

    // 처음 조회 시(cursor가 null) 가장 큰 값을 주어 최신 데이터부터 가져오게 함
    Long actualCursor = (cursor == null) ? Long.MAX_VALUE : cursor;

    // 다음 페이지가 있는지 확인하기 위해 요청된 10개보다 1개 더 많은 11개를 조회합니다!
    Pageable limit = PageRequest.of(0, 11);

    List<IncorrectNoteDto> notes;
    if (category == null) {
      // 카테고리가 없으면 전체 오답 노트 최신순 조회
      notes = answerRepository.findAllIncorrectNotesByCursor(userId, actualCursor, limit);
    } else {
      // 카테고리가 있으면 해당 카테고리만 필터링해서 조회
      notes = answerRepository.findIncorrectNotesByCategoryAndCursor(userId, category, actualCursor, limit);
    }

    boolean hasNext = false;
    Long nextCursor = null;

    // 조회된 결과가 11개라면 다음 페이지가 존재한다는 의미!
    if (notes.size() > 10) {
      hasNext = true;
      notes = notes.subList(0, 10); // 프론트에는 10개만 잘라서 내려줌
      nextCursor = notes.get(9).getNoteId(); // 마지막 10번째 데이터의 ID를 다음 커서로 지정
    }

    return IncorrectNoteListResponse.builder()
        .totalCount(totalCount)
        .totalBytes(totalBytes)
        .notes(notes)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
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

    // 챕터 아이디로 챕터 제목을 가져오기
    String chapterTitle = chapterRepository.findById(note.getChapterId())
        .map(chapter -> chapter.getTitle())
        .orElse("알 수 없는 챕터");

    // 총 오답 개수 및 보유 바이트
    int totalCount = answerRepository.countByUserIdAndIsCorrectFalse(userId);

    // 실제 유저 정보 조회 및 보유 바이트 연동
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    int totalBytes = user.getTotalBytes();

    // 기존 Learning 도메인의 QuizInfo 재사용
    QuizInfo reusableQuizInfo = QuizInfo.withoutAnswer(quiz);

    return IncorrectNoteDetailResponse.builder()
        .totalCount(totalCount)
        .totalBytes(totalBytes)
        .noteId(note.getId())
        .chapterId(note.getChapterId())
        .chapterTitle(chapterTitle)
        .createdAt(note.getCreatedAt())
        .userAnswer(note.getSelectedAnswer())
        .correctAnswer(quiz.getCorrectAnswer())
        .explanation(quiz.getExplanation())
        .quiz(reusableQuizInfo)
        .build();
  }
}