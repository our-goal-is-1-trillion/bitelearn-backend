package com.ogi1t.bitelearn.domain.user.entity;

import com.ogi1t.bitelearn.domain.auth.entity.enums.ProviderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // 생성시간 자동 주입
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(length = 255)
  private String password;

  @Column(nullable = false, unique = true, length = 50)
  private String nickname;

  @Column(length = 10)
  private Integer age;

  // ★ 소셜 로그인 종류 (GOOGLE, NAVER) 추가
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ProviderType providerType;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  // 온보딩 완료 여부 추가 (기본값 false)
  @Column(nullable = false)
  @Builder.Default
  private boolean isOnboardingCompleted = false;

  @Column(nullable = false)
  private int totalBytes = 0; // 유저가 보유한 총 바이트 (기본값 0)

  // 바이트 적립 메서드
  public void addBytes(int bytes) {
    this.totalBytes += bytes;

    // 방어 로직: 총 보유 바이트가 0 밑으로 떨어지지 않게 막아줌
    if (this.totalBytes < 0) {
      this.totalBytes = 0;
    }
  }

  // 현재 레벨 계산 메서드 (2000 / 4000 / 6000 기준)
  public int getLevel() {
    if (this.totalBytes < 2000) {
      return 1;
    } else if (this.totalBytes < 4000) {
      return 2;
    } else {
      return 3; // 4000 이상은 모두 레벨 3 (만렙)
    }
  }

  // 닉네임 변경 편의 메서드
  public void updateNickname(String newNickname) {
    this.nickname = newNickname;
  }

  // 온보딩 완료 편의 메서드 추가
  public void completeOnboarding() {
    this.isOnboardingCompleted = true;
  }
}