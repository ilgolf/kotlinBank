# 코틀린을 이용한 핀테크 서비스

## 진행 이유

코틀린을 사용하여 토이 프로젝트를 진행하며 코틀린에 친숙해지고 싶은 목적으로 진행한 프로젝트. 이외에 핀테크 서비스 또는 e-커머스의 결제 모듈에 대한 관심이 있어 관련 도메인으로 선정하여 작업
또한 서비스 특성상 비동기나 다양한 비즈니스 기술적 고민이 가능하기 때문에 경험해보고싶어서 진행 

## ⚙️Tech Stack

Back-End : Kotlin + Spring boot + JWT + Spring Security

Database : Redis + MySQL

Infra (etc) : Docker + RabbitMQ

## 기능 요구사항 

notion URL : https://www.notion.so/dev-golf/Banking-0b00d27d7be14690a44a77610cb4f5f9 

## UML

<img width="1126" alt="image" src="https://user-images.githubusercontent.com/77387861/229456761-02353690-7a47-4756-81ed-b802d0db61a1.png">


## Core API Process (payment & refund)

### implementation Policy

서비스 이용자가 타 플랫폼을 통해 우리 서비스로 구매 요청 시 결제 요청을 대신 은행에 보내준다. 또한 결제 후 히스토리를 쌓아 CS 대응을 빨리 할 수 있게 대응책을 마련해놓는다.

```kotlin
override fun pay(finAccount: String, transferMoney: BigDecimal): String {
    return webClient.post()
        .uri(URI.create(NhUrlUtils.PAYMENT_URL))
        .bodyValue(SimplePaymentRequestDto.of(finAccount, transferMoney))
        .retrieve()
        .onStatus(
            { status -> status.is4xxClientError || status.is5xxServerError },
            { response ->
                Mono.error(
                    SimplePaymentFailException(response.statusCode().reasonPhrase)
                )
            }
        )
        .bodyToMono<SimplePaymentResponseDto>()
        .onErrorResume { error ->
            NhPaymentApiClient.log.error("Payment failed: {}", error.message)
            Mono.just(SimplePaymentResponseDto.createDefault(finAccount))
        }
        .flux()
        .toStream()
        .findFirst()
        .orElseGet { SimplePaymentResponseDto.createDefault(finAccount) }
        .header
        .responseMessage
}
```

NH에서는 https://developers.nonghyup.com/guide/GU_1000 따로 못받아온 사유를 응답 메시지에 담아 알려주고 있기 때문에 이 정보를 기반으로 HistoryDto 객체를 생성하여 실패 사유를 함께 저장해준다.
400 or 500 에러 시 로그로 오류를 확인할 수 있게 ErrorResume을 통해 에러 핸들링을 하였으며 Default 객체를 생성하여 실패에 대한 메시지를 Default 메시지로 주어 다른 사유로 실패한 경우와 구분했습니다.

## etc implementation Design

### Get Balance

잔액 조회를 할 때에 외부 서비스를 이용하므로 영향도를 마찬가지로 없애야함 다만 이 때에는 잔액을 필히 받아와야하고 MQ로 처리할 정도로 재시도 전략이 필요하지 않고 또한 못받아 왔을 때 특정한 값을 보내줘야하므로 동기로 처리하되 에러 핸들링을 통해 영향도를 제거

또한 API를 두 개로 나눠 Redis에 존재하는 가장 최근에 조회된 정보 또는 ???? 값을 화면에 먼저 보여주고 이후 Lazy하게 다른 API에서 외부 서비스인 NH에서 잔액 정보를 최신화 하여 받아옴

### Get FinAccount

FinAccount는 농협에 핀테크 서비스를 이용할 수 있도록 도와주는 필수 로직 이 또한 계좌 개설 시 외부 서비스에서 받아와야하기 때문에 에러 핸들링으로 영향도 제거 MQ를 사용하여 비동기로 처리하기에 계좌 생성은 반드시 사용자가 성공 유무를 알아야 하기 때문에 동기로 처리 

## TODO

1. 비관적락을 이용한 동시성 제어
2. 사용자 피드백 알람 서비스를 도입하여 처리 (SSE + Redis pub/sub) WebSocket STOMP
3. 결제 실패 시 slack 연동
4. OptimisticLockException catch 하여 정합성 오류기 때문에 따로 핸들링 !! 비관적락 vs 낙관적락 결제 일관성 (토스 결제) 추가로 낙관적락/비관적락 왜쓸까에 대한 고민 추가

참고한 포스팅 : https://medium.com/@odysseymoon/spring-webclient-%EC%82%AC%EC%9A%A9%EB%B2%95-5f92d295edc0
