### 📅 화상영어 수강신청 시스템 (Course Registration)

과정, 수업 요일, 시작일, 시간을 고르면 **32회차 수업 일정이 자동으로 만들어지는** 화상영어 수강신청 웹 애플리케이션입니다.

더존ICT그룹 인턴십 과제로 6주 동안 진행했습니다. 매주 피드백을 받아 반영하며 요구사항 분석, 프론트엔드·백엔드 구현, 클라우드 배포까지 혼자 진행했습니다.

> 과제 기간에는 Vercel(프론트엔드)과 GCP Cloud Run + Cloud SQL(백엔드)에 배포해 운영했습니다. 현재는 비용 문제로 서버를 내린 상태입니다.

<br>

### 데모 스크린샷
![수강신청 시연](https://github.com/user-attachments/assets/c994744e-8e0b-46a1-958d-d485f36ff5d7)

<br>

---

## ✨ 주요 기능

* **32회차 수업 일정 자동 생성**
  * 시작일과 요일 패턴(월수금 / 화목)을 받아, 주말을 빼고 32회의 수업 날짜를 계산합니다.
  * 신청 1건과 수업 32건을 하나의 트랜잭션으로 저장해서, 중간에 실패하면 전부 롤백됩니다.
* **강사별 예약 시간 관리**
  * 과정을 고르면 담당 강사가 자동으로 배정됩니다.
  * 해당 강사의 날짜별 예약된 시간을 조회해, 화면에서 이미 찬 시간은 선택할 수 없게 막습니다.
* **동시 신청 시 중복 예약 방지**
  * 같은 강사의 같은 날짜·시간에는 수업이 하나만 존재하도록 DB 유니크 제약으로 보장합니다.
  * 동시에 신청이 들어와도 한 명만 성공하고, 나머지는 `409 Conflict`를 받습니다.
* **분리 배포**
  * 프론트엔드는 Vercel, 백엔드는 Google Cloud Run, DB는 Cloud SQL(MySQL)에 배포했습니다.

<br>

---

## 🛠️ 기술 스택

* **Backend:** Java 17, Spring Boot 3.2, Spring Data JPA, springdoc-openapi(Swagger)
* **Database:** MySQL (GCP Cloud SQL)
* **Test:** JUnit 5, H2 (MySQL 호환 모드)
* **Frontend:** React, Tailwind CSS, Axios, react-datepicker
* **Deployment:** Google Cloud Run (Backend), Vercel (Frontend)

<br>

---

## 📡 API

| Method | URL | 설명 |
|---|---|---|
| `POST` | `/api/enrollments` | 수강 신청 (32회차 수업 생성). 이미 찬 시간이면 `409 Conflict` |
| `GET` | `/api/sessions/booked-times?date=&instructorId=` | 강사의 해당 날짜 예약된 시간 목록 |
| `GET` | `/api/instructors/{instructorId}/available-times` | 강사의 수업 가능 시간 |

서버를 실행한 뒤 `/swagger-ui/index.html`에서 전체 명세를 볼 수 있습니다.

<br>

---

## 📁 폴더 구조

```
Course-Registration
├── coursepage   # 프론트엔드 (React)
└── sugang-api   # 백엔드 (Spring Boot)
```

<br>

---

## 🚀 실행 방법

### 1. 저장소 클론

```bash
git clone https://github.com/gityhb/Course-Registration.git
cd Course-Registration
```

### 2. 백엔드 실행 (Spring Boot, JDK 17)

`sugang-api/src/main/resources/application.properties.example`을 참고해 로컬 MySQL 접속 정보를 담은 `application.properties`를 만든 뒤 실행합니다. 이 파일은 Git에 올라가지 않습니다.

```bash
cd sugang-api
./gradlew bootRun
```

테스트는 인메모리 DB(H2)를 쓰기 때문에 MySQL 없이 실행할 수 있습니다.

```bash
./gradlew test
```

### 3. 프론트엔드 실행 (React)

`coursepage/src/registration.js`의 `API_URL`을 로컬 백엔드 주소(`http://localhost:8080`)로 바꾼 뒤 실행합니다.

```bash
cd coursepage
npm install
npm start
```

<br>

---

## 🧠 문제 해결

### 1. 동시 신청 시 같은 시간이 중복 예약되는 문제

* **문제**
  * 화면에서는 이미 찬 시간을 막았지만, 서버는 예약 여부를 확인하지 않고 저장만 하고 있었습니다.
  * 두 사람이 거의 동시에 같은 시간을 신청하면 둘 다 성공할 수 있었습니다.
* **재현**
  * 10개 스레드가 같은 강사, 같은 시간에 한꺼번에 신청하는 테스트를 작성했습니다.
  * 수정 전에는 10건이 모두 성공했습니다.
* **해결**
  * "먼저 조회하고 비어 있으면 저장"하는 방식은 두 요청이 동시에 조회하면 둘 다 통과하기 때문에 쓰지 않았습니다.
  * 대신 `class_sessions`의 (강사, 날짜, 시간)에 **유니크 제약**을 걸어 DB가 최종적으로 막게 했습니다. 서버를 여러 대로 늘려도 그대로 동작합니다.
  * 제약 위반은 `DuplicateBookingException`으로 바꿔 `409 Conflict`로 응답합니다. 신청과 수업 32건이 같은 트랜잭션이라 실패한 신청은 남지 않습니다.
* **결과**
  * 같은 테스트에서 1건만 성공하고, 저장된 데이터도 신청 1건과 수업 32건으로 정확했습니다.
  * 이 과정에서 `build.gradle`에 `useJUnitPlatform()`이 빠져 테스트가 한 번도 실행되지 않던 설정도 찾아 고쳤습니다.

### 2. 분리 배포 후 CORS 오류

* **문제:** 프론트엔드(Vercel)와 백엔드(Cloud Run)를 서로 다른 도메인에 배포하자 브라우저가 API 요청을 막았습니다.
* **해결:** 전역 CORS 설정(`WebConfig`)을 추가하고 배포 도메인을 허용 목록에 넣었습니다.

### 3. 설정 파일에 DB 비밀번호가 커밋된 문제

* **문제:** 배포용 설정 파일에 DB 비밀번호가 그대로 들어간 채 공개 저장소에 올라가 있었습니다.
* **해결:** 비밀번호를 환경변수(`DB_PASSWORD`)로 분리하고, `git filter-repo`로 커밋 기록 전체에서 지웠습니다.
