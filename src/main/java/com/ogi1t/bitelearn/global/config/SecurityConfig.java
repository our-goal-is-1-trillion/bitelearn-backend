package com.ogi1t.bitelearn.global.config;

import com.ogi1t.bitelearn.domain.auth.service.CustomOAuth2UserService;
import com.ogi1t.bitelearn.global.security.JwtAuthenticationFilter;
import com.ogi1t.bitelearn.global.security.JwtProvider;
import com.ogi1t.bitelearn.global.security.OAuth2SuccessHandler;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProvider jwtProvider;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ⭐ CORS 설정 적용
        .csrf(AbstractHttpConfigurer::disable) // REST API이므로 csrf 보안 필요 없음
        .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
        .formLogin(AbstractHttpConfigurer::disable) // 기본 폼 로그인 비활성화
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음 (Stateless)

        // 요청에 대한 권한 설정
        .authorizeHttpRequests(auth -> auth
            // 인증 없이 접근 가능한 경로
            .requestMatchers(
                "/index.html/**",
                "/ws-stomp/**",
                "/auth/**", // 소셜 관련 경로 허용
                "/auth/**", // 소셜 관련 경로 허용
                "/login/**", // 소셜 관련 경로 허용
                "/oauth2/**", // 소셜 관련 경로 허용
                "/oauth/**", // 소셜 관련 경로 허용
                "/v3/api-docs/**", // 스웨거
                "/swagger-ui/**", // 스웨거
                "/swagger-ui.html") // 스웨거
            // 그 외 모든 요청은 인증 필요
            .permitAll()
            .anyRequest().authenticated()
        )

        // 소셜 로그인 설정
        .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(endpoint -> endpoint
                .baseUri("/oauth/login")
            )
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService) // 유저 정보 가져오는 서비스 등록
            )
            .successHandler(oAuth2SuccessHandler)     // 로그인 성공 시 처리 핸들러 등록
        )

        // JWT 필터 등록
        .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // ⭐ CORS 상세 설정 Bean 추가
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 프론트엔드 로컬과 배포 주소 모두 허용
    configuration.setAllowedOrigins(Arrays.asList(
        "https://www.bitelearn.site",
        "http://localhost:5173",
        "https://bitelearn.vercel.app"
    ));

    // 허용할 HTTP 메서드
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

    // 허용할 헤더
    configuration.setAllowedHeaders(List.of("*"));

    // ⭐ 쿠키 등 인증 정보를 주고받을 수 있도록 허용
    configuration.setAllowCredentials(true);

    // 클라이언트가 응답 헤더 중 Authorization 등을 볼 수 있도록 노출
    configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); // 모든 API 경로에 이 설정 적용
    return source;
  }
}