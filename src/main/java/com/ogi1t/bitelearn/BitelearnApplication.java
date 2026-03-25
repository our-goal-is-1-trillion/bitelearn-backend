package com.ogi1t.bitelearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class BitelearnApplication {

  public static void main(String[] args) {
    SpringApplication.run(BitelearnApplication.class, args);
    System.out.println("\n" +
        "=================================================\n" +
        "👮🚨🏃🏽‍➡️ Bitelearn Application 시작 완료!\n" +
        "=================================================\n" +
        "📋 Swagger UI: http://localhost:8080/swagger-ui/index.html\n" +
        "=================================================\n");
  }
}