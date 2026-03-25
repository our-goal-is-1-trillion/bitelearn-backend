package com.ogi1t.bitelearn.domain.dashboard.service;

import com.ogi1t.bitelearn.domain.dashboard.dto.response.RecommendedChapterResponse;
import com.ogi1t.bitelearn.domain.learning.entity.Chapter;
import com.ogi1t.bitelearn.domain.learning.entity.enums.Topic;
import com.ogi1t.bitelearn.domain.learning.repository.ChapterRepository;
import com.ogi1t.bitelearn.domain.learning.repository.UserQuizAnswerRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

  private final ChapterRepository chapterRepository;
  private final UserQuizAnswerRepository answerRepository;

  public List<RecommendedChapterResponse> getRandomRecommendations(Long userId) {
    // 1. 유저가 시도한 챕터 ID 목록 조회
    List<Long> attemptedChapterIds = answerRepository.findAttemptedChapterIdsByUserId(userId);

    // 2. 전체 챕터 조회
    List<Chapter> allChapters = chapterRepository.findAll();

    // 3. 유저가 이미 건드린 '토픽' 목록 추출
    Set<Topic> touchedTopics = allChapters.stream()
        .filter(c -> attemptedChapterIds.contains(c.getId()))
        .map(Chapter::getTopic)
        .collect(Collectors.toSet());

    // 4. 아직 풀지 않은 전체 챕터 필터링
    List<Chapter> unattemptedChapters = allChapters.stream()
        .filter(c -> !attemptedChapterIds.contains(c.getId()))
        .collect(Collectors.toList());

    // 5. '아예 안 건드린 토픽'과 '건드리긴 했지만 남은 챕터가 있는 토픽' 분리
    List<Chapter> untouchedTopicChapters = unattemptedChapters.stream()
        .filter(c -> !touchedTopics.contains(c.getTopic()))
        .collect(Collectors.toList());

    List<Chapter> touchedTopicChapters = unattemptedChapters.stream()
        .filter(c -> touchedTopics.contains(c.getTopic()))
        .collect(Collectors.toList());

    // 6. 각 토픽별로 가장 앞 번호(sequence)의 챕터만 후보로 선정 후 섞기
    List<Chapter> candidates = new ArrayList<>();
    candidates.addAll(pickFirstChaptersAndShuffle(untouchedTopicChapters)); // 우선순위 1: 완전 새로운 토픽
    candidates.addAll(pickFirstChaptersAndShuffle(touchedTopicChapters));   // 우선순위 2: 풀다 만 토픽

    // 7. 상위 2개 추출하여 DTO 변환 (남은 챕터가 1개 이하면 있는 만큼만 반환)
    return candidates.stream()
        .limit(2)
        .map(RecommendedChapterResponse::from)
        .collect(Collectors.toList());
  }

  // 토픽별로 가장 순서가 빠른 챕터 1개씩만 뽑아서 랜덤으로 섞어주는 헬퍼 메서드
  private List<Chapter> pickFirstChaptersAndShuffle(List<Chapter> chapters) {
    Map<Topic, Optional<Chapter>> minSeqByTopic = chapters.stream()
        .collect(Collectors.groupingBy(Chapter::getTopic,
            Collectors.minBy(Comparator.comparing(Chapter::getSequence))));

    List<Chapter> distinctChapters = minSeqByTopic.values().stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    Collections.shuffle(distinctChapters); // 랜덤 추천을 위한 셔플
    return distinctChapters;
  }
}