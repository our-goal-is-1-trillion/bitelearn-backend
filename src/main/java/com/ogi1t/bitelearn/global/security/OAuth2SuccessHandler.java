package com.ogi1t.bitelearn.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogi1t.bitelearn.domain.auth.entity.RefreshToken;
import com.ogi1t.bitelearn.domain.auth.repository.RefreshTokenRepository;
import com.ogi1t.bitelearn.domain.user.entity.User;
import com.ogi1t.bitelearn.domain.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;

  @Value("${app.frontend-redirect-url}")
  private String frontendRedirectUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    // 1. 로그인된 유저 정보 가져오기
    CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

    log.info("소셜 로그인 성공! 이메일: {}", principal.getEmail());

    // 2. DB에서 유저 조회하여 닉네임 가져오기
    User user = userRepository.findById(principal.getUserId())
        .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

    // 3. JWT 토큰 생성
    String accessToken = jwtProvider.createAccessToken(
        user.getId(),
        user.getEmail(),
        user.getNickname(),
        user.getProviderType()
    );

    String refreshToken = jwtProvider.createRefreshToken(
        user.getId()
    );

    // 4. Refresh Token DB 저장
    refreshTokenRepository.deleteByUserId(user.getId());
    refreshTokenRepository.save(RefreshToken.builder()
        .userId(user.getId())
        .token(refreshToken)
        .createdAt(LocalDateTime.now())
        .expiredAt(LocalDateTime.now().plusDays(14))
        .build());

    // 쿠키 설정
    // 1. Refresh Token을 HttpOnly 쿠키로 설정
    ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
        .maxAge(14 * 24 * 60 * 60) // 14일
        .path("/")
        .secure(true) // HTTPS 적용으로 true로 변경
        .sameSite("None") // HTTPS 적용으로 None으로 변경
        .httpOnly(true)
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    // 만료 시간 설정
    long accessTokenExpiresIn = 1800; // 1800초 (30분)

    // 2. UriComponentsBuilder를 사용해 안전하게 URL 생성
    String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUrl)
        .queryParam("accessToken", accessToken)
        .queryParam("accessTokenExpiresIn", accessTokenExpiresIn)
        .build()
        .toUriString();

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}