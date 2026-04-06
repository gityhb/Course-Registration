### 🛒 수강신청 시스템 (Course Registration System)

대규모 트래픽이 발생하는 대학교 수강신청 환경을 가정하여 설계한 풀스택 웹 애플리케이션입니다.

더존ICT그룹 인턴십 과제로 총 6주간 진행되었으며, 요구사항 분석부터 프론트엔드/백엔드 구현, 클라우드 환경 배포까지 웹 개발의 전체 사이클을 거쳐 완성되었습니다. 사용자(학생)가 장바구니에 강의를 담고 실시간으로 수강신청을 완료할 수 있도록 지원합니다.

<br>

### 데모 스크린샷
![수강신청 시연](https://github.com/user-attachments/assets/c994744e-8e0b-46a1-958d-d485f36ff5d7)

<br>

---

## ✨ 주요 기능

* **실시간 수강신청 및 장바구니:** Axios를 활용한 REST API 통신으로 지연 없이 실시간 잔여 좌석 및 강의 목록을 렌더링합니다.
* **직관적인 사용자 인터페이스:** React Hooks(`useState`, `useEffect`, `useCallback`)를 활용하여 렌더링을 최적화하고, Tailwind CSS로 깔끔한 UI를 구현했습니다.
* **안정적인 비즈니스 로직 처리:** Spring Boot 백엔드 서버를 통해 수강신청, 예비 담기 등 핵심 로직의 데이터 정합성을 철저하게 관리합니다.
* **클라우드 인프라 배포:** 프론트엔드와 백엔드를 각각 Vercel과 Google Cloud Run을 통해 성공적으로 분리 배포했습니다.
* **구조화된 코드 관리:** `public`과 `src` 폴더를 루트에 명확히 분리하여 체계적인 레포지토리 형상 관리를 유지했습니다.

<br>

---

## 🛠️ 기술 스택

* **Frontend:** React.js, Tailwind CSS, Axios
* **Backend:** Spring Boot (Java)
* **Database:** MySQL, GCP Cloud SQL
* **Deployment:** Vercel (Frontend), Google Cloud Run (Backend)
* **Version Control:** Git, GitHub

<br>

---

## 🚀 프로젝트 실행 방법

### 1. 저장소 클론

```bash
git clone [https://github.com/gityhb/Course-Registration.git](https://github.com/gityhb/Course-Registration.git)
cd Course-Registration
```

### 2. Frontend 실행 (React)

```bash
# 의존성 패키지 설치
npm install

# 로컬 서버 실행
npm start
```

### 3. Backend 실행 (Spring Boot)

```bash
# 별도의 Java IDE (IntelliJ IDEA 등)에서 프로젝트를 열람한 후,
# Gradle/Maven을 통해 빌드 및 애플리케이션(Application.java) 실행
```

<br>

---

## 🧠 핵심 문제 해결

* **문제 1**: 프로젝트 초기 세팅 시, React의 `public` 및 `src` 폴더가 루트 디렉토리에 위치하지 않아 배포 및 GitHub 레포지토리 관리에 구조적인 어려움이 발생했습니다.
* **해결 1**: 로컬 환경에서 디렉토리 구조를 올바르게 재배치한 후, 레포지토리를 재생성하고 코드를 푸시하는 방식으로 해결하여 직관적이고 효율적인 형상 관리 환경을 구축했습니다.

* **문제 2**: 클라우드 환경에서 프론트엔드와 백엔드, 그리고 데이터베이스가 안정적으로 통신해야 했습니다.
* **해결 2**: GCP Cloud SQL을 도입하여 MySQL 데이터베이스를 클라우드에 띄우고, 백엔드는 Google Cloud Run, 프론트엔드는 Vercel을 사용하여 각 서비스의 특성에 맞는 맞춤형 인프라 배포를 진행하여 안정적인 통신 환경을 구축했습니다.
