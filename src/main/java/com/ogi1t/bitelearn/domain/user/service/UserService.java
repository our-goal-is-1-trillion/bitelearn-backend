package com.ogi1t.bitelearn.domain.user.service;

import com.ogi1t.bitelearn.domain.user.dto.request.NicknameUpdateRequest;
import com.ogi1t.bitelearn.domain.user.dto.response.UserResponse;
import com.ogi1t.bitelearn.domain.user.entity.User;
import com.ogi1t.bitelearn.domain.user.repository.UserRepository;
import com.ogi1t.bitelearn.global.exception.BusinessException;
import com.ogi1t.bitelearn.global.exception.domain.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용
public class UserService {

  private final UserRepository userRepository;

  // 1. 내 정보 조회
  public UserResponse getMyInfo(Long userId) {
    User user = findUserById(userId);
    return UserResponse.from(user);
  }

  // 2. 닉네임 수정
  @Transactional // 쓰기 작업이므로 Transactional 필요
  public void updateNickname(Long userId, NicknameUpdateRequest request) {
    User user = findUserById(userId);
    String newNickname = request.getNickname();

    // 닉네임 중복 검사 (내 닉네임과 같으면 통과)
    if (!user.getNickname().equals(newNickname) && userRepository.existsByNickname(newNickname)) {
      throw new BusinessException(UserErrorCode.NICKNAME_DUPLICATION);
    }

    // 엔티티 변경 (Dirty Checking으로 자동 저장됨)
    user.updateNickname(newNickname);
  }

  // 3. 온보딩 완료 처리
  @Transactional
  public void completeOnboarding(Long userId) {
    User user = findUserById(userId);
    user.completeOnboarding(); // 상태를 true로 변경
  }

  // 공통 유저 조회 메서드
  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
  }
}