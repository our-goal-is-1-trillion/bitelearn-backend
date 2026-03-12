package com.ogi1t.bitelearn.domain.auth.controller;

import com.ogi1t.bitelearn.domain.auth.dto.request.LoginRequest;
import com.ogi1t.bitelearn.domain.auth.dto.request.SignupRequest;
import com.ogi1t.bitelearn.domain.auth.dto.response.TokenResponse;
import com.ogi1t.bitelearn.domain.auth.service.AuthService;
import com.ogi1t.bitelearn.global.exception.BusinessException;
import com.ogi1t.bitelearn.global.exception.domain.AuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "로컬 회원가입")
  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
    authService.signup(request);
    return ResponseEntity.ok("회원가입이 완료되었습니다.");
  }

  @Operation(summary = "로컬 로그인")
  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    TokenResponse tokenResponse = authService.login(request);

    // 로컬 로그인 시에도 Refresh Token은 HttpOnly 쿠키로 굽기
    setRefreshTokenCookie(response, tokenResponse.getRefreshToken(), 14 * 24 * 60 * 60);

    return ResponseEntity.ok(tokenResponse);
  }

  @Operation(summary = "토큰 재발급")
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(
      @CookieValue(name = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {

    // 1. 쿠키 자체가 없는 비회원일 경우
    if (refreshToken == null || refreshToken.isEmpty()) {
      throw new BusinessException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    TokenResponse tokenResponse = authService.refresh(refreshToken);

    setRefreshTokenCookie(response, tokenResponse.getRefreshToken(), 14 * 24 * 60 * 60);
    return ResponseEntity.ok(tokenResponse);
  }

  @Operation(summary = "로그아웃")
  @PostMapping("/logout")
  public ResponseEntity<String> logout(
      @CookieValue(name = "refreshToken", required = false) String refreshToken,
      HttpServletResponse response) {

    if (refreshToken != null) {
      authService.logout(refreshToken);
    }

    // 쿠키 무효화 (만료시간 0)
    setRefreshTokenCookie(response, "", 0);

    return ResponseEntity.ok("로그아웃 되었습니다.");
  }

  // 쿠키 생성 공통 메서드
  private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAge) {
    ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
        .maxAge(maxAge)
        .path("/")
        .secure(true) // HTTPS 이므로 true
        .sameSite("None") // HTTPS 이므로 None 설정 가능
        .httpOnly(true)
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}