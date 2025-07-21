# LOGGING-PRACTICE
LOGGING-PRACTICE REPOSITORY 

<hr>

## 목적
로그 저장을 위해 ELK 파이프라인 구축을 실습하기 위한 레포지토리 입니다.   
(LogStash, Elasticsearch, Kibana)

## 실습 이유

<details>

<summary> MSA 개선 - Kafka와 LogService 구현 </summary>

<img width="750" height="612" alt="최종 아키텍처" src="https://github.com/user-attachments/assets/4bf8a0a6-3ff9-40be-bf9d-31a3061f93ee" />

</details>

졸업 프로젝트를 개선하고자 Kafka와 LogService를 추가했다.   
해당 서버 아키텍처를 현직자분에게 피드백 받았는데, 긍정적이진 않았다. Kafka에서 외부 로그 저장소에 Log 데이터를 날려주는 방식이 아니라 LogService라는 MicroService가 동작하는 게 어색하다고 하셨다.   
- RDB에 저장하면 데이터 분석가의 주 언어인 SQL를 활용하기 좋다고 생각해서 RDB에 저장하는 방식으로 구현했었지만 실무에서는 RDB에 Log를 잘 적재하지 않는다고한다!!
- 그도 그럴 것이 RDB중 MySQL을 사용했는 데 이는 MongoDB와 비교했을 때 Insert 삽입 성능에 있어서 큰 차이가 있다 (Data 10,000개 부터 성능 격차가 심하다.)
  - PostgreSQL은 RDB와 NoSQL의 장점을 합친 DB로 알고 있는데 여기에 저장하는 건 괜찮으려나...?

이런 피드백을 받고 로그 적재 파이프라인으로 유명한 ELK를 실습해보기로 했다.   

기존에는 타 `Microservice` -> `Logging AOP` -> `Kafka` -> `LogService` -> `Log DB 적재` 같은 FLOW를 사용했다.   
여기서 피드백을 반영하기 위해 `Logback.xml` -> `Logstash` -> `Elasticsearch 적재` 같은 FLOW로 단순화 했고, LogService가 가져다준 어색한 파이프라인을 제거하고자 했다.   
또한 Elasticsearch 시각화 도구 Kibana를 사용해 로그 분석에 용이하도록 하겠다!   



<hr>

## 실습 환경

### 1. 로컬 환경 실습 X

- ELK를 실제 프로젝트에 적용하기 위해서는 결국 실제 서버에 적용하는 게 좋다고 생각한다!
- 사용 중인 노트북은 Galaxy Book Pro 5으로 Docker 적절하지 않은 환경입니다.   
  - Docker Desktop이 설치되어 있으나, WSL 기반이므로, 리소스 소모가 크다!   


### 2. Server - Raspberri Pi 4 선택

- 선택 이유 1 - Elasticsearch 최소 램 조건(4G)를 만족한다.
- 선택 이유 2 - 내가 무료로 사용할 수 있는 서버 조건이다.
- 선택 이유 3 - 램 조건 4G를 만족하는 클라우드 서비스는 비싸다...!

### 3. CI/CD 파이프라인도 구축

- 연습 환경이지만, 실제 프로젝트라고 연습하고자 한다. 따라서 ISSUE, PR, COMMIT 단위를 고려하면서 실습했다!
- Main 브랜치에 PR이 Merge 될 때, 실제 서버가 어떻게 변하고 어떤 트러블이 일어나는 지 확인하기 위해 CI/CD 파이프라인까지 구축해 실습했다!

<hr>

## 컨벤션

실제 프로젝트라고 생각하고 컨벤션도 정하여, 준수하기 위해 노력했습니다.

### ISSUE 컨벤션

- 형식: `[ISSUE 단위] "할 일"   `
- 예시: `[Practice] Logstash ElasticSearch 도입`

### ISSUE 단위

- `Build`: 초기 세팅, 파이프라인 구축 관련 기능
- `Feat`: 내가 이미 알고 있지만, 프로젝트 입장에서 추가되는 기능
- `Practice`: 내가 연습하고자 하는 기능

### PR 컨벤션 
- 상동

### PR 단위
- 상동

### COMMIT 컨벤션

- 형식: `Commit Type: commit message`
- 예시: `feat: logback.xml 추가` 

### COMMIT 단위

- `feat`: 기능 구현
- `fix`: 코드 수정
- `remove`: 코드 제거
- `build`: 초기 세팅, 파이프라인 구축, 빌드 관련 기능

<hr>

## ELK 파이프라인

<img width="900" height="610" alt="Log 데이터 파이프라인" src="https://github.com/user-attachments/assets/c5c5e856-2851-4efa-bf76-d2fd89981d40" />

### ELK FLOW

1. Logback.xml에서 LogstashAppender 설정.
2. LogstashAppender가 Log 정보를 Logstash로 보냄.
3. Logstash가 logstash.conf에 정의된 대로 Elasticsearch로 Log를 보냄.
4. Elasticsearch에 적재된 Log를 편하게 볼 수 있도록 시각화 도구 Kibana 사용

<hr>


## 실습 후기

개인적으로 kafka를 구동했을 때 보다 Raspberry Pi의 Memory가 간당간당해서 놀랐다. Swap 설정까지 했는데 이정도일 줄이야..!   

단기간 실습해보니 ELK는 확실히 강력한 로그 적재 파이프라인이다. 간단한 설정으로 로그를 적재할 수 있으며, 많은 데이터를 쉽게 시각화할 수 있다. 이를 무료로 이용할 수 있다는 게 가장 큰 이점!   
실제 운영중인 프로젝트에 적용하면 정말 큰 도움이 될거같다!!   

### Logstash

Logback.xml을 통해 Logstash로 Log보내주고 Logstash가 Elasticsearch로 보내주는 중요한 미들웨어 역할이다.   
logstash.conf 파일도 input, ouput으로 구성되어, 직관적이다.
한가지 의문은 Logstash가 죽어버리는 상황에서는 대응책이 있는 가? -> 없다면 Logstash에 너무 의존적이진 않은가?   
  - 찾아보니 Logback 설정에서 Logstash로 보내지 못한 로그는 메모리에 임시로 버퍼링 할 수 있다고 한다. docker 를 사용한다면 restart 옵션, k8s를 사용한다면 restartPolicy를 사용하는 게 좋을 거 같다!

### Elasticsearch

docker compose.yml 에서 적용할 설정도 많지 않고 간편해서 좋았다. 단기간 실습을 위해 사용하다 보니 신기하다는 느낌이 많았다. 그래서 궁금해서 단점을 찾아봤는데 로그 수집량과 저장기간이 늘어날수록 리소스와 관리 비용이 늘어난다고 한다. 이에 대안으로 LGTM의 Loki가 있다고 한다. 실제 Tave나 가치택시에 적용해보면서 관리 비용에 부담이 느껴진다면 Grafana 생태계도 공부해보면서 Loki를 실습해보면 좋을 거 같다!

### Kibana

데이터 분석은 SQL이 가장 좋지 않을 까 했다. Kibana 홈페이지에 들어가면서 간단한 DashBoard를 생성하고 여러 조건으로 필터링을 걸 수 있다는 게 신기했다.   
물론 기존에 알고있는 SQL에 비해서 학습하는 비용이 있겠지만, 데이터 분석가라고 해도 Kibana 사용법을 거부하진 않겠다 싶을 정도로 잘 만들어진 프레임워크라고 느껴졌다.
시계열, 특정 로그 레벨, 특정 에러 같은 필터링 조건 실습을 진행했다!


