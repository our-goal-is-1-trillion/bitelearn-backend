package com.ogi1t.bitelearn.domain.user.controller;

import com.ogi1t.bitelearn.domain.user.dto.request.NicknameUpdateRequest;
import com.ogi1t.bitelearn.domain.user.dto.response.UserResponse;
import com.ogi1t.bitelearn.domain.user.service.UserService;
import com.ogi1t.bitelearn.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "유저 정보 조회 및 수정")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 상세 정보를 조회합니다.")
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal CustomPrincipal principal) {
    // principal.getUserId()는 우리가 만든 CustomPrincipal에서 가져옴 (JWT 파싱 결과)
    UserResponse response = userService.getMyInfo(principal.getUserId());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "닉네임 수정", description = "로그인한 사용자의 닉네임을 변경합니다. (중복 불가)")
  @PatchMapping("/me/nickname")
  public ResponseEntity<String> updateNickname(@AuthenticationPrincipal CustomPrincipal principal, @RequestBody NicknameUpdateRequest request) {
    userService.updateNickname(principal.getUserId(), request);
    return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
  }

  @Operation(summary = "온보딩 완료 처리", description = "로그인한 사용자의 온보딩 완료 상태를 true로 변경합니다.")
  @PatchMapping("/me/onboarding")
  public ResponseEntity<String> completeOnboarding(@AuthenticationPrincipal CustomPrincipal principal) {
    userService.completeOnboarding(principal.getUserId());
    return ResponseEntity.ok("온보딩이 완료 처리되었습니다.");
  }
}