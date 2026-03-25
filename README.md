# 📚 BiteLearn Backend
> 사회 초년생을 위한 부동산·금융·세무 마이크로 러닝 플랫폼, **BiteLearn**의 백엔드 리포지토리

<a href="https://www.bitelearn.site"><img src="https://img.shields.io/badge/🌐_BiteLearn_Service-배포_링크-5A58FF?style=for-the-badge" alt="Service Link" /></a>

<img width="700" alt="image" src="https://github.com/user-attachments/assets/b9b1f28a-5c93-4a65-a4f7-a2e6087b36fe" />

<br></br>

## 📑 목차
- [🔗 관련 링크 및 레퍼런스](#-관련-링크-및-레퍼런스)
- [👥 팀 및 협업](#-팀-및-협업)
- [🛠️ 기술 스택](#️-기술-스택)
- [🗄️ 데이터베이스 모델링](#️-데이터베이스-모델링)
- [🏗️ 시스템 아키텍처](#️-시스템-아키텍처)
- [📁 패키지 구조](#-패키지-구조)
- [✨ 핵심 기능](#-핵심-기능)
- [🔥 트러블슈팅](#-트러블슈팅)
- [🚀 성능 개선 및 튜닝](#-성능-개선-및-튜닝)

<br>

## 🔗 관련 링크 및 레퍼런스
<table>
  <tr>
    <td align="center" width="120">
      <a href="https://www.notion.so/goormkdx/1-2eac0ff4ce31802681bbd64b9d7f8c0d"><img src="https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png" width="50" alt="Notion Icon"></a>
    </td>
    <td width="800">
      <b><a href="https://www.notion.so/goormkdx/1-2eac0ff4ce31802681bbd64b9d7f8c0d">🏠 팀 노션 (Team Workspace)</a></b><br>
      기획, 디자인, 프론트/백엔드의 협업 기록과 회의록 확인
    </td>
  </tr>
  <tr>
    <td align="center" width="120">
      <a href="https://www.notion.so/goormkdx/API-2eac0ff4ce3181909832f4c10165b91b"><img src="https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png" width="50" alt="API Specs Icon"></a>
    </td>
    <td width="800">
      <b><a href="https://www.notion.so/goormkdx/API-2eac0ff4ce3181909832f4c10165b91b">📝 API 명세서 (API Specification)</a></b><br>
      프론트엔드 개발자와의 원활한 협업을 위해 작성한 API 요청 및 응답 명세서
    </td>
  </tr>
  <tr>
    <td align="center" width="120">
      <a href="https://bitelearn.site/swagger-ui/index.html"><img src="https://upload.wikimedia.org/wikipedia/commons/a/ab/Swagger-logo.png" width="60" alt="Swagger Icon"></a>
    </td>
    <td width="800">
      <b><a href="https://bitelearn.site/swagger-ui/index.html">🚀 배포 및 API 테스트 (Swagger UI)</a></b><br>
      백엔드 서버에 직접 요청을 보내고 응답을 확인할 수 있는 API 명세 및 테스트 환경
    </td>
  </tr>
</table>
<br>

## 👥 팀 및 협업
> 기획, 디자인, 프론트엔드, 백엔드가 긴밀하게 소통하며 애자일(Agile)하게 개발한 **협업 프로젝트**입니다.<br>
> 단독 백엔드 개발자로서 시스템 아키텍처 설계부터 인프라 배포, API 개발까지 전 과정을 책임지고 수행했습니다.

| 역할 (Role) | 인원 | 멤버 이름 |
| :--- | :---: | :--- |
| **👑 PM (Product Manager)** | 3명 | 이성민, 장세혁, 한보름 |
| **🎨 PD (Product Designer)** | 2명 | 박시원, 은석기 |
| **💻 FE (Front-End)** | 1명 | 최아로인 |
| **⚙️ BE (Back-End)** | 1명 | 백유정 |

<br>

## 🛠️ 기술 스택

### Backend
* **Java 17 & Spring Boot 3.x** : 객체지향적 설계 및 풍부한 생태계를 활용한 안정적인 비즈니스 로직 구현
* **Spring Data JPA & QueryDSL** : 객체 중심의 도메인 설계(ORM) 도입 및 유지보수성 향상, 커서 기반 무한 스크롤 등 복잡한 동적 쿼리의 타입 세이프(Type-Safe)한 처리

### Database
* **MySQL (Amazon RDS)** : 복잡한 학습 기록(퀴즈, 오답 노트)과 유저 데이터 간의 엄격한 데이터 정합성 보장

### Infrastructure & DevOps
* **AWS (EC2, ALB, Route 53, S3)**
  * `Route 53 + ALB`: 전 구간 HTTPS 암호화 통신 구축 및 트래픽 분산
  * `S3`: 미디어 파일(이미지 등) 분리 저장을 통한 서버 무상태(Stateless) 아키텍처 실현
* **Docker & Jenkins** : 컨테이너화를 통한 실행 환경 의존성 제거 및 CI/CD 파이프라인 자동화로 수동 배포 시 발생하는 휴먼 에러 방지

### Security & Auth
* **Spring Security & JWT** : 무상태(Stateless) 기반의 빠르고 안전한 인증/인가 시스템 구축
* **OAuth 2.0 (Google, Naver)** : 사용자 가입 이탈률 최소화 및 민감 정보 보안 리스크 외부 위임
* **보안 최적화** : Refresh Token에 `HttpOnly`, `Secure`, `SameSite=None` 쿠키 정책을 적용하여 XSS 및 CSRF 공격 원천 방어

<br>

## 🗄️ 데이터베이스 모델링

<img width="500" alt="bitelearn ERD Diagram" src="https://github.com/user-attachments/assets/d066d290-f130-4260-8813-f752f26880df" />


### 데이터베이스 모델링 및 설계 주안점
1. **확장성을 고려한 유저-소셜 계정 분리 (`users` & `oauth_accounts`)**
   유저 테이블에 소셜 정보를 종속시키지 않고 `oauth_accounts` 테이블을 1:N 연관 관계로 분리하여, 하나의 로컬 계정에 여러 개의 소셜 계정을 연동할 수 있는 확장성을 확보했습니다.
2. **다형성을 고려한 JSON 타입 활용 (`quiz.specificData`)**
   다양한 퀴즈 타입(객관식, 대화형, 문서 클릭형 등)에 따라 필요한 데이터 규격이 다릅니다. 이를 수많은 컬럼이나 상속 맵핑으로 낭비하는 대신, MySQL의 `JSON` 타입을 활용하여 유연하게 데이터를 저장하도록 모델링했습니다.
3. **정적 데이터의 Enum 처리로 JOIN 최소화 (`Category`, `Topic`)**
   변경이 잦지 않은 대/중분류 카테고리를 DB 테이블 대신 애플리케이션 레벨의 Enum 타입으로 관리하여 DB I/O 부하를 줄이고 읽기 성능을 최적화했습니다.
4. **Stateless 아키텍처를 보완하는 토큰 관리 (`refresh_tokens`)**
   JWT 탈취 위험 방지 및 안전한 유효기간 관리를 위해 `refresh_tokens` 테이블을 설계하고 만료 시간(`expiredAt`)을 저장하여, 서버 단에서 세션을 주도적으로 제어할 수 있는 보안 토대를 마련했습니다.

<br>

## 🏗️ 시스템 아키텍처

<img width="700" alt="bitelearn 시스템 아키텍처 diagram" src="https://github.com/user-attachments/assets/1c0f455c-0342-44ef-87a1-e2ec329ebc46" />


### 시스템 아키텍처 설계 주안점
1. **프론트/백엔드 완전 분리 및 Vercel 배포**: 클라이언트는 Vercel의 글로벌 CDN망을 통해 빠르게 서빙하고, 데이터 트래픽은 AWS 인프라로 분리하여 각 서버의 부하를 독립적으로 제어합니다.
2. **전 구간 HTTPS 및 보안 통신 구축**: Route 53으로 도메인을 관리하고, AWS ALB에 ACM 인증서를 부착해 브라우저부터 로드밸런서까지 안전한 HTTPS 암호화 통신을 구현했습니다. (소셜 로그인 Mixed Content 및 쿠키 SameSite 보안 이슈 해결)
3. **AWS S3 연동을 통한 Stateless 서버 지향**: 퀴즈 및 단어장에 사용되는 미디어 이미지 파일들을 EC2 내부에 저장하지 않고 S3로 분리하여 서버의 디스크 I/O 부하를 줄이고 Scale-out 제약 사항을 해소했습니다.
4. **Jenkins & Docker 기반 CI/CD 파이프라인**: GitHub에 코드가 Push 되면 Webhook을 통해 Jenkins가 빌드를 트리거하고, Docker 이미지로 패키징하여 배포까지 원클릭으로 이어지는 견고한 자동화를 구축했습니다.

<br>

## 📁 패키지 구조

```text
src/main/java/com/ogi1t/bitelearn
├── global                      # 전역 설정 및 공통 관심사(Cross-Cutting)
│   ├── config                  # Security, CORS, Swagger, JPA Auditing 설정
│   ├── exception               # GlobalExceptionHandler, Custom Error Codes
│   └── security                # JWT Provider, Custom UserDetailsService
│
└── domain                      # 비즈니스 도메인별 모듈 분리 (DDD-lite)
    ├── auth                    # 로그인, 회원가입, JWT, 소셜(OAuth) 계정 매핑
    ├── user                    # 유저 정보, 마이페이지, 재화(Bytes) 관리
    ├── learning                # 챕터, 퀴즈(객관식/OX), 단어장, 학습 진행도
    └── note                    # 오답 노트 (스냅샷 패턴 적용)
        │
        ├── controller          # REST API 진입점 (@RestController)
        ├── service             # 핵심 비즈니스 로직 (@Transactional)
        ├── repository          # DB 접근 레이어 (Spring Data JPA)
        ├── entity              # JPA 엔티티 및 Enum (Category, Topic 등)
        └── dto                 # Request/Response 및 Validation(@Valid) 객체
```

<br>

## ✨ 핵심 기능

### 1. 회원 및 보안 시스템 (Auth & Security)
* **통합 로그인 시스템**: 자체(Local) 회원가입 및 소셜 로그인(Google, Naver OAuth 2.0) 통합 구현
* **보안 토큰 관리**: JWT 기반 인증 및 XSS/CSRF 방어를 위한 `HttpOnly`, `Secure` 설정의 Refresh Token 쿠키 발급
* **안전한 회원 관리**: 정규식을 활용한 비밀번호 유효성 검증 및 회원 정보(닉네임, 온보딩 상태) 관리 API 제공

### 2. 마이크로 러닝 및 퀴즈 (Learning & Quiz)
* **계층형 학습 구조**: 4대 카테고리(부동산, 생활금융, 세무, 자산운용) 기반의 체계적인 챕터 및 토픽 제공
* **단어카드 학습**: 챕터별 핵심 키워드를 예습할 수 있는 양면 단어장 제공
* **다양한 퀴즈 시스템**: 일반 객관식, 대화형 OX, 문서 클릭형 등 5가지 퀴즈 유형(`QuizType`)에 맞춘 유연한 JSON 데이터 모델링 및 채점 로직 구현

### 3. 데이터 무결성 기반 오답 노트 (Incorrect Note)
* **스냅샷 패턴 적용**: 퀴즈 원본 데이터 수정·삭제 시에도 유저의 과거 학습 기록이 보존되도록 비정규화(Denormalization)된 데이터 스냅샷 저장
* **무한 스크롤 최적화**: 대용량 데이터 조회를 대비해 커서 기반 페이지네이션(No-Offset)을 적용
* **맞춤형 복습**: 특정 카테고리 필터링 조회 및 프론트엔드 렌더링을 위한 챕터 시퀀스(단원 번호) 동기화 제공

### 4. 학습 진행도 및 게이미피케이션 (Progress & Gamification)
* **실시간 진행도 트래킹**: 유저의 챕터별 학습 상태(시작 전, 진행 중, 완료) 및 마지막 풀이 위치 자동 저장
* **이어하기 기능**: 마이페이지에서 가장 최근 학습 챕터와 진행률(%)을 계산하여 즉시 이어갈 수 있는 동적 데이터 응답
* **보상 및 레벨 시스템**: 학습 완료 시 자체 재화 '바이트(Bytes)' 적립 및 누적 바이트 기반 자동 레벨 산정 로직 구현

<br>

## 🔥 트러블슈팅

### 1. 인프라 아키텍처 고도화: 전 구간 HTTPS 통신 구축 및 보안 쿠키 정책 수립
* **문제 상황**: 소셜 로그인 시 로컬(HTTPS)과 백엔드(HTTP) 간 프로토콜 불일치로 Mixed Content 에러가 발생. 또한 Refresh Token을 Body로 전달 시 XSS 공격 탈취 위험이 존재했습니다.
* **해결 과정**:
  * **AWS 기반 HTTPS 환경 구축**: Route 53 커스텀 도메인 + ACM 인증서 + ALB를 통해 브라우저부터 로드밸런서까지 안전한 HTTPS 암호화 통신(443)을 구현하고, 백엔드로는 HTTP(80)로 포워딩하는 아키텍처를 설계했습니다.
  * **토큰 탈취 방지 쿠키 정책**: Refresh Token을 HTTP Response Cookie로 전면 수정. 스프링 시큐리티에 개입하여 `HttpOnly`, `Secure`, `SameSite=None` 속성을 정밀하게 부여했습니다.
* **결과**: 도메인 간 프로토콜 불일치 및 CORS 문제를 근본적으로 해결하고, XSS/CSRF 공격을 사전 차단하는 높은 수준의 보안 아키텍처를 완성했습니다.

### 2. 대용량 데이터를 고려한 커서 기반 페이지네이션 및 무한 스크롤 API 설계
* **문제 상황**: 초기 오답 노트 목록 API는 오프셋(Offset) 방식으로 설계되어, 페이지가 뒤로 갈수록 성능 저하($O(N)$ 시간 복잡도)와 실시간 데이터 변동 시 정합성 문제(중복 노출/누락)가 우려되었습니다.
* **해결 과정**: 
  * **No-Offset 쿼리 튜닝**: 마지막으로 조회한 데이터의 PK를 기준점(Cursor)으로 사용하는 페이지네이션을 도입. `WHERE id < :cursor ORDER BY id DESC LIMIT 10` 형태로 쿼리를 개선하여 $O(1)$ 수준의 조회 성능을 확보했습니다.
  * **클라이언트 친화적 응답 설계**: 프론트엔드 구현을 돕기 위해 응답 DTO에 `nextCursor`와 `hasNext` 필드를 추가. 실제 요청 사이즈보다 1개를 더 조회(11개)하는 로직으로 불필요한 DB Count 쿼리 호출을 방지했습니다.
* **결과**: 데이터 증가에도 일정한 응답 속도를 보장하며, 데이터 변동 상황에서도 중복 없는 매끄러운 UX를 제공하게 되었습니다.

### 3. 마이크로서비스 관점의 도메인 결합도 완화: 비정규화를 통한 데이터 무결성 확보
* **문제 상황**: 오답 노트가 원본 퀴즈 엔티티의 ID만 FK로 참조하도록 설계했으나, 관리자가 퀴즈를 수정/삭제할 경우 유저의 과거 오답 기록까지 변형되거나 에러가 발생하는 강결합 문제가 발견되었습니다.
* **해결 과정**:
  * **스냅샷 패턴 및 비정규화 도입**: 유저가 퀴즈를 채점하는 시점에 원본 데이터(질문, 보기, 해설 등)를 `QuizInfo`라는 별도 모델로 추출(비정규화)했습니다.
  * **데이터 격리**: 추출된 스냅샷 데이터를 오답 노트 도메인에 안전하게 저장하여 읽기 성능(조인 최소화)을 높이고, '학습 기록 보존'이라는 비즈니스 요구사항을 충족시켰습니다.
* **결과**: 퀴즈와 오답 노트 도메인의 생명주기를 분리하여, 마스터 데이터 변동에도 유저의 개인 학습 기록 무결성이 100% 보장되는 견고한 시스템을 구축했습니다.

<br>

## 🚀 성능 개선 및 튜닝

### 대규모 트래픽 대비 오답 노트 무한 스크롤 조회 성능 79% 개선 (0.5초 ➡️ 0.07초)
* **문제 상황 (병목 발견):** 오답 노트 목록 조회 API에 커서 기반(No-Offset) 페이지네이션을 적용하여 N+1 문제를 방어했음에도 불구, JMeter 부하 테스트(Users: 100명) 결과 최대 응답 시간이 665ms까지 치솟으며 프리티어 EC2의 CPU 사용률이 95%에 달하는 병목 현상이 발생했습니다.
* **원인 분석 (진짜 범인 찾기):**
  1. **DB Full Scan:** MySQL이 `is_correct = false`인 조건과 `id < cursor` 조건을 찾기 위해 수만 건의 데이터를 풀스캔(Full Scan)하며 정렬까지 수행하고 있었습니다.
  2. **커넥션 풀(HikariCP) 대기:** 풀스캔으로 인해 단일 쿼리 처리 시간이 길어지면서, 기본 설정인 10개의 DB 커넥션이 모두 고갈되었습니다. 이로 인해 후속 190여 개의 요청들이 커넥션을 얻기 위해 대기(Acquisition Time)하면서 전체 응답 시간이 기하급수적으로 늘어난 것이었습니다.
* **해결 과정 (DB 인덱스 튜닝 및 부하 테스트 검증):**
  * **복합 인덱스(Composite Index) 추가:** `UserQuizAnswer` 엔티티에 유저 ID, 오답 여부, 그리고 페이징 정렬을 위한 ID를 묶은 복합 인덱스(`idx_user_correct_id`)를 설계하여 적용했습니다.
    ```java
    @Table(name = "user_quiz_answer", indexes = {
        @Index(name = "idx_user_correct_id", columnList = "user_id, is_correct, id DESC")
    })
    ```
  * **불필요한 Count 쿼리 제거 (Service 단 최적화):** 첫 페이지 로딩 시에만 전체 카운트를 계산하고, 스크롤(두 번째 페이지)부터는 카운트 쿼리(`countByUserIdAndIsCorrectFalse`) 호출을 생략하도록 로직을 개선하여 불필요한 DB I/O를 차단했습니다.
  * **JVM 웜업(Warm-up) 및 인덱스의 역설 인지:** 초기 테스트 시 캐시가 적재되지 않은 콜드 스타트(Cold Start) 상태이거나 데이터 모수가 적을 때 오히려 성능이 저하되는 현상을 발견했습니다. 이를 통제하기 위해 Before/After 각각 2회 이상의 반복 부하 테스트를 진행하여 객관적인 지표를 확보했습니다.
* ✅ **결과 (지표 개선):**
  * **CPU Usage:** 0.907 ➡️ **0.455 (약 50% 감소)**
  * **Response Time:** 342ms ➡️ **70.9ms (약 79% 단축)**
  결과적으로 인덱스 탐색(Index Seek)을 통해 쿼리 실행 계획을 최적화하고, 커넥션 풀의 병목을 완벽하게 해소하여 시스템 처리량(Throughput)을 극대화했습니다.

<br>

<img width="1572" height="623" alt="부하테스트 결과 캡처본_01" src="https://github.com/user-attachments/assets/e9be97e5-1a07-476f-93cb-946dbee397d4" />
<img width="1583" height="622" alt="부하테스트 결과 캡처본_02" src="https://github.com/user-attachments/assets/69ea2a7a-8736-47fb-b432-b39ac1c134bc" />
<img width="1577" height="569" alt="부하테스트 결과 캡처본_03" src="https://github.com/user-attachments/assets/25f3cfd9-8ba2-4605-bb21-89136826a47b" />
<img width="1575" height="564" alt="부하테스트 결과 캡처본_04" src="https://github.com/user-attachments/assets/6eaacf32-7e5b-4dec-af0c-b5e97e54ed97" />
