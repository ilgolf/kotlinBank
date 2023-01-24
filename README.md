# 코틀린 Banking

### 회원 정책

회원 가입 시 필요한 정보 

- email
- password
- passwordConfirm
- name
- nickname
- birth
- profileImage (required = false)
- phoneNumber
- isPhoneCheck

계좌 생성은 따로 한다. 비밀번호 확인 정보까지 받는다.

회원 수정

- name, nickname, profileImage, birth 4가지만 바꾸도록 한다.
- 비밀번호는 따로 인증을 통해 바꾼다. (기존 비밀번호로 재 인증)
- 비밀 번호 변경 → 휴대폰 인증 → 새 비밀번호 작성

회원 삭제

회원 삭제 시 db에는 히스토리로 남긴다. 다만 회원을 애플리케이션에서 조회할 순 없어야한다.

회원 상세 조회 

- email
- name
- nickname
- birth
- profileImage
- phoneNumber

정보들을 조회할 수 있다. 

회원 검색 조회

- 닉네임, email로 조회를 할 수 있고 빈칸으로 조회하면 아무것도 나오지 않아야한다.
- 검색한 회원은 간단한 정보들만 볼 수 있다. (profileImage, nickname, email)
- 검색한 회원에게 송금이 가능하다.
- 조회 시 like ‘xxx%’ 식으로 조회할 수 있다. (쌍 라이크 x)

휴대폰 본인 인증 

- coolSMS를 이용 우리가 문자로 보낸 정보를 그대로

### 인증 정책

- JWT 토큰으로 인증한다. 토큰에는 회원 ID, 권한 정보가 들어있어야 한다.
- 토큰은 access, refresh 두 토큰으로 나뉘며 refresh는 아무 정보가 들어있지 않아야 한다.
- accessToken은 유효시간이 3시간이며 3시간 후에는 refreshToken으로 재발행해야한다.
- logout시 두 토큰 모두 삭제되며 로그인 된 사용자만 가능하다.

### 계좌 정책

- 회원이 여러 계좌를 갖고 있을 수 있으며 서로 계좌정보를 갖고 송금이 가능하다.
- 계좌를 조회할 땐 계좌 비밀번호가 필요 없지만 나머지 업무는 반드시 계좌 비밀번호가 필요하다.

계좌 생성

계좌 생성 시 필요한 정보

- number
- password
- member
- bankName
- nickname

계좌는 수정할 수 없다.

이 때 농협 오픈 api를 통해 발급한 key를 같이 저장해야한다.

계좌 송금

- 농협 open api를 이용하여 송금한다.

계좌 조회

- 계좌 정보를 조회할 수 있다.
- 최근 입 출금 히스토리를 조회할 수 있다. (최근 10건 기준 페이징)

잔액 조회

- 잔액을 확인할 수 있다. (농협 open api 이용)

계좌 삭제

- 회원 삭제와 동일

계좌 전체 조회

- 계좌 정보를 간략하게 조회한다.

### 계좌 히스토리

- 삭제 수정 불가능하다.
- 입출금 금액 정보와 거래한 사람의 이름과 거래한 시간을 저장한다.


# 핵심 로직

--- 

## 계좌 정보 간략 조회

### 요구 사항

사용자가 홈 화면에서 자신의 갖고있는 계좌에 대한 정보를 간략하게 볼 수 있습니다. 해당 서비스에 필요한 정보는 다음과 같습니다.

- 잔액
- 계좌 은행 (국민, 신한 등등 ...)
- 계좌 별칭

### 초기 개발 방향성

- 잔액 조회는 오픈 api인 NH api에서 finAccount를 요청으로 보내 가져온다.
- 계좌 은행과 별칭은 MySQL DB에서 가져온다.
- 이 후 데이터를 합쳐 응답에 담아 반환한다.

### 외부 API 서버 장애 시 대처에 대한 방법

- 인터페이싱을 통해 장애 났을 때 빠르게 다른 동작을 하는 API를 `@Bean` 으로 바꾸어 처리하는 방법 (PASS)
    - 이 방법은 결국 장애가 났을 때 배포 프로세스를 거쳐야합니다. 이는 꽤나 번거로운 작업이 될 수 있습니다.
    - 비즈니스에 결국 완전한 영향을 없앤 것이 아닙니다. (Bean을 수동으로 계속해서 변경해주어야함)
- Message Broker를 이용하여 외부 api에 데이터를 가져옴 (고민 중…)
    - Message Queue를 컨트롤 하여 fail over에 대해 처리해줄 수 있고 현재 많은 기업에서 사용하고 있는 솔루션
    - 상당한 오버 엔지니어링 (인프라 추가 + fail over 등 너무 많은 작업이 소요)
    - Message Broker에 대한 현재 지식도 부족함
- 2step으로 구성을 분리하여 redis와 외부 api에서 데이터를 보여주는 방법(git Comment를 통해 배움)



![Untitled (1)](https://user-images.githubusercontent.com/77387861/214322343-cce4f10e-70b7-49aa-9deb-3f8bfc64756b.png)



- Server-side에서 캐시 형태로 저장하여 보관
- 1차로 캐시 데이터에서 조회하여 당장에 화면을 보여줌 (이 때 데이터 신뢰성은 좀 떨어짐 하지만 화면상 불편성 제거)
- 2차로 외부 연동된 API를 호출하여 실시간으로 데이터를 가져옴 이 때 가져왔을 때 Redis cache에 넣어주고 가져오지 못하면 에러 메시지를 내보내 현재 실시간 데이터가 아님을 사용자에게 전달
- 기본 응답 초기 값은 `???,???` 형태로 제공
- 이 후 데이터가 점차 쌓여가면 scale-out or scale-up 고려

- 동일한 요청이 가더라도 처리 시간에  따라 순서가 뒤집히거나 데이터 정합성 (처리 시간의 처리 시간) 문제를 해결 어떻게 할 것인가?
    - 요청 타임스탬프를 기록했다가 타임스탬프를 이용해 filtering하는 방법을 사용
    - 농협에다가 요청을 날린 History 테이블을 추가하여 예상치 못한 장애에 대응할 수 있게 준비


# 개발 시 주의점

- 반드시 외부 api는 현재 비즈니스 로직에 영향을 주지 않게 설계 및 개발하여야한다. 
(사유 : 외부 api는 현재 우리가 다루는 product가 아니다. 외부 api 버그로인해 비즈니스에 영향을 받으면 언제 해결될 지 모르는 버그 때문에 우리 비즈니스가 진행이 안될 수 있기 때문에 그렇다.)
