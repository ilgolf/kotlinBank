# 코틀린을 이용한 핀테크 서비스

## ⚙️Tech Stack

Back-End : Kotlin + Spring boot + JWT + Spring Security

Database : Redis + MySQL

Infra (etc) : Docker + RabbitMQ

## 기능 요구사항 

notion URL : https://www.notion.so/dev-golf/Banking-0b00d27d7be14690a44a77610cb4f5f9 

## UML

<img width="1126" alt="image" src="https://user-images.githubusercontent.com/77387861/229456761-02353690-7a47-4756-81ed-b802d0db61a1.png">


## USE CASE 

## Core API Process (payment & refund)

### implementation Policy

서비스 이용자가 타 플랫폼을 통해 우리 서비스로 구매 요청 시 결제 요청을 대신 은행에 보내준다. 또한 결제 후 히스토리를 쌓아 CS 대응을 빨리 할 수 있게 대응책을 마련해놓는다.
다만, 외부 은행에 결제요청을 보낼 때 비동기로 처리하여 독립적으로 실행 되게 구현 또한 결제 요청만 완료시키고 API는 종료 시킬 것

### RabbitMQ의 사용

사전 배경으로 깔아야 할게 있습니다. 우선 최초엔 2가지를 고려해봤습니다. @Async 등을 이용하여 비동기로 독립적으로 처리하는 방법과 동기로 처리하되 에러핸들링을 통하여 영향을 받지 않게 하는 방법이었습니다.

1. @Async는 따로 Thread pool 자원을 사용해야하는데 이 때 대기 Queue에 너무 많이 쌓이면 Exception이 발생하기 때문에 비즈니스에 영향을 줄 수 있습니다. 또한 Queue 사이즈를 늘리기에는 하나의 애플리케이션 서버에서 동작하기 때문에 한계가 존재합니다. 또한 실패 시 재시도가 불가능하며 한 번 오류가 나면 그 뒤에 요청까지 영향을 받을 수 있습니다. 

2. 에러 핸들링만으로 동기로 처리하더라도 영향도를 줄일 순 있지만 DB 히스토리 저장이나 예상치 못한 작업에서 오류가 나면 그 동안 했던 모든 작업들이 멈춰 버립니다. 또한 많은 요청이 쏟아질 경우 작업 속도가 느려질 수 있고 Error를 전부 흘리고 있기 때문에 실행만하고 종료해 버리는 요구사항 특성상 오류를 재 때 잡아내어 처리하지 못할 수도 있습니다. 

RabbitMQ는 그에 반해 비동기로 독립적으로 동작할 수 있게 해주어 외부 서비스로 부터 내부 서비스가 영향을 받는 경우를 없애주며 따로 동작하는 하나의 인프라 요소란점 따로 디스크에 저장할 수 있는점을 고려해봤을 때 왠만한 요청에 버틸 수 있습니다. 설령 부하가 걸리더라도 자동 클러스터링이나 액티브 스탠바이 모드등을 이용하여 부하에 대한 대처가 가능합니다.

또한 RabbitMQ는 실패 시 재시도를 통해 일시적인 장애로 인해 못받아오는 경우를 피할 수 있고 이는 세부 설정으로 주기를 정할 수 있습니다. 예를 들어 1분간 5번의 시도를 할 수 있게 만들 수 있으며 그렇게 해도 실패한다면 심각한 장애로 판단 빠르게 DLX로 보내어 메시지를 Disk에 저장하고 장애를 추적해볼 수 있습니다. 

### Design Structure

![image](https://user-images.githubusercontent.com/77387861/229472955-c69ee095-90a3-4e0c-b6e5-87ca2d463f9e.png)


오해할 수 있는데 Queue는 클러스터링 된게 아니고 RabbitMQ 하나에 두 개의 큐가 있는 상태이며 Spring Boot도 두 개로 되어있지만 표현상에 이유이며 로직을 보면 실제로는 하나로 이루어져 있습니다.


## etc implementation Design
