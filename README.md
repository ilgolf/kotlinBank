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
