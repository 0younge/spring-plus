# SPRING PLUS

Spring Boot 기반 일정 관리 API 프로젝트입니다.  
JWT 인증, 사용자 권한 관리, 일정·댓글·담당자 관리, QueryDSL 검색, 트랜잭션 분리 로그 저장 기능을 제공합니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.3.3 |
| Security | Spring Security, JWT |
| Data | Spring Data JPA, QueryDSL 5.0.0 |
| Database | H2 (기본) / MySQL |
| Build | Gradle |
| Etc | Lombok |

---

## 주요 기능

### 인증 / 인가
- 회원가입 · 로그인 · JWT 발급 및 인증 처리
- `/auth/**` — 인증 없이 접근 가능
- `/admin/**` — `ADMIN` 권한 필요

### 사용자
- 사용자 단건 조회
- 비밀번호 변경
- 관리자 권한 변경

### 일정
- 일정 생성 / 단건 조회 / 목록 조회 / 페이지네이션
- 날씨 조건 검색, 수정일 기준 기간 검색
- 일정 생성 시 외부 날씨 API로 오늘의 날씨 저장

### 댓글
- 댓글 등록 / 목록 조회

### 담당자
- 담당자 등록 / 목록 조회 / 삭제
- 일정 작성자는 본인을 담당자로 등록 불가
- 담당자 등록·삭제는 일정 작성자만 가능

### 일정 검색 (QueryDSL)

검색 조건: 일정 제목 키워드, 생성일 시작·종료일, 담당자 닉네임

응답 포함 정보: 일정 제목, 담당자 수, 댓글 수

### 트랜잭션 로그
- 담당자 등록 요청 시 `log` 테이블에 요청 로그를 저장
- `REQUIRES_NEW` 전파 옵션으로 독립 트랜잭션 처리 — 담당자 등록 실패 시에도 로그는 별도 저장

---

## 실행 방법

```bash
# 1. 클론
git clone https://github.com/0younge/spring-plus.git
cd spring-plus

# 2. 실행 (기본 포트 8080)
./gradlew bootRun

# 3. 테스트
./gradlew test
```

---

## 환경 변수

기본 실행 환경은 H2 인메모리 DB입니다. MySQL 사용 시 아래 환경 변수를 설정하세요.

| 변수명 | 설명 | 기본값 |
|--------|------|--------|
| `SERVER_PORT` | 서버 포트 | `8080` |
| `DB_URL` | DB 접속 URL | H2 in-memory |
| `DB_DRIVER` | DB 드라이버 | `org.h2.Driver` |
| `DB_USERNAME` | DB 사용자명 | `sa` |
| `DB_PASSWORD` | DB 비밀번호 | 빈 값 |
| `DDL_AUTO` | Hibernate DDL 옵션 | `update` |
| `JWT_SECRET_KEY` | JWT 시크릿 키 | 기본값 존재 |

---

## API 목록

**Auth**

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/auth/signup` | 회원가입 |
| POST | `/auth/signin` | 로그인 |

**User**

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/users/{userId}` | 사용자 조회 |
| PUT | `/users` | 비밀번호 변경 |
| PATCH | `/admin/users/{userId}` | 사용자 권한 변경 |

**Todo**

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/todos` | 일정 생성 |
| GET | `/todos` | 일정 목록 조회 |
| GET | `/todos/{todoId}` | 일정 단건 조회 |
| GET | `/todos/search` | 일정 검색 |

**Comment**

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/todos/{todoId}/comments` | 댓글 등록 |
| GET | `/todos/{todoId}/comments` | 댓글 목록 조회 |

**Manager**

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/todos/{todoId}/managers` | 담당자 등록 |
| GET | `/todos/{todoId}/managers` | 담당자 목록 조회 |
| DELETE | `/todos/{todoId}/managers/{managerId}` | 담당자 삭제 |

---

## 프로젝트 구조

```
src/main/java/org/example/expert
├── client
│   └── WeatherClient
├── config
│   ├── JwtFilter
│   ├── JwtUtil
│   ├── QueryDslConfig
│   └── SecurityConfig
└── domain
    ├── auth
    ├── comment
    ├── common
    ├── log
    ├── manager
    ├── todo
    └── user
```
