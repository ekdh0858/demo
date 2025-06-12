# Demo Spring Boot REST API

간단한 **회원가입/로그인/권한관리** 기능과 **JWT 인증**, **Swagger 기반 API 문서**, **메모리 데이터 저장소**를 제공하는 프로젝트입니다.
## 🌐 배포

* EC2, Amazon Linux 등 환경에서
  `java -jar <파일명>.jar`
  명령어로 바로 실행 가능

  배포 주소 : 3.35.49.129:8080
  swagger : 3.35.49.129:8080/swagger-ui/index.html

---

## ⚡ 프로젝트 특징

* **Spring Boot** 기반 REST API
* **JWT**를 활용한 인증 및 인가
* **메모리 기반** UserRepository(별도 DB 불필요)
* **Swagger (OpenAPI)** UI 제공 (API 명세/테스트 지원)
* 회원가입, 로그인, 관리자 권한 부여 등 핵심 기능 구현

---

## 🛠️ 실행 방법

### 1. 빌드

프로젝트 루트에서

```bash
./gradlew build
# 또는
gradle build
```

### 2. 실행

```bash
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

> JAR 파일명은 빌드 환경에 따라 다를 수 있습니다

### 3. API 문서(Swagger) 확인

서버 기동 후 브라우저에서
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
로 접속

---

## 🚀 주요 API 명세

### 1. 회원가입

* **POST** `/signup`
* **Request Body**

  ```json
  {
    "username": "JINHO",
    "password": "12341234",
    "nickname": "Mentos"
  }
  ```
* **Response**

  ```json
  {
    "id": 1,
    "username": "JINHO",
    "nickname": "Mentos",
    "roles": [ { "role": "USER" } ]
  }
  ```

### 2. 로그인

* **POST** `/login`
* **Request Body**

  ```json
  {
    "username": "JINHO",
    "password": "12341234"
  }
  ```
* **Response**

  ```json
  {
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9..."
  }
  ```

> 발급받은 JWT 토큰을 이후 인증이 필요한 요청에 사용하세요.

### 3. 관리자 권한 부여

* **PATCH** `/admin/users/{userId}/roles`
* **Header:**
  `Authorization: Bearer <JWT_TOKEN>`
* **Response**

  ```json
  {
    "username": "JINHO",
    "nickname": "Mentos",
    "roles": [ { "role": "ADMIN" } ]
  }
  ```

---

## 🛡️ 인증/인가 및 에러 응답

* 인증 필요 API 요청에 토큰이 없거나 유효하지 않으면:

  ```json
  {
    "error": {
      "code": "INVALID_TOKEN",
      "message": "유효하지 않은 인증 토큰입니다."
    }
  }
  ```

* 권한 없는 사용자가 접근 시:

  ```json
  {
    "error": {
      "code": "ACCESS_DENIED",
      "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
    }
  }
  ```

---

## 🧪 테스트

* JUnit5, MockMvc 기반 단위 테스트 제공
* `UserControllerTest` 등에서 회원가입/로그인/권한 관련 다양한 시나리오를 검증합니다.

---


## 📄 기타 참고

* 본 프로젝트는 교육/학습 목적의 샘플로, 영속 DB가 없어 데이터는 서버 재시작 시 초기화됩니다.
* API 명세 및 테스트는 `/swagger-ui/index.html`에서 직관적으로 확인 가능
