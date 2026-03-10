package com.ogi1t.bitelearn.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

  @Bean
  @Primary // 다른 ObjectMapper가 있더라도 이 설정을 우선 사용하도록 합니다.
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    // LocalDateTime 같은 날짜 타입을 처리하기 위한 모듈 등록
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
}