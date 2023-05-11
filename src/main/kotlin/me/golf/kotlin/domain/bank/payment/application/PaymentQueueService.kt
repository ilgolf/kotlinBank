package me.golf.kotlin.domain.bank.payment.application

import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.payment.dto.RefundRequestDto
import me.golf.kotlin.domain.bank.payment.dto.TransferRequestDto
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value

/**
 * 데이터 정합성 이슈와 사용자 즉각 피드백 안되는 문제 이슈 등으로 해당 기능 잠시 Bean 제외
 *
 * Lock이 정말 필요할까?
 *
 * 1. 비관적락을 이용한 동시성 제어
 * 2. 사용자 피드백 알람 서비스를 도입하여 처리 (SSE + Redis pub/sub) WebSocket STOMP
 * 3. 결제 실패 시 slack 연동
 * 4. OptimisticLockException catch 하여 정합성 오류기 때문에 따로 핸들링 !! 비관적락 vs 낙관적락 결제 일관성 (토스 결제) 추가로 낙관적락/비관적락 왜쓸까에 대한 고민 추가
 *
 * 요구사항 다시 정의하고 고민해보자
 */
class PaymentQueueService(
    private val bankAccountRepository: BankAccountRepository,
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${message.queue.payment-queue}") private val paymentQueueName: String,
    @Value("\${message.queue.refund-queue}") private val refundQueueName: String,
    @Value("\${message.queue.topic-key}") private val topicKey: String
): PaymentService {

    override fun pay(requestDto: TransferRequestDto) {
        val dto = bankAccountRepository.findFinAccountBy(requestDto.bankId)
            ?: throw BankAccountException.NotFoundException()

        val messageRequestDto = requestDto.toPaymentMessageRequestDto(dto.finAccount)

        rabbitTemplate.convertAndSend(topicKey, paymentQueueName, messageRequestDto)
    }

    override fun refund(requestDto: RefundRequestDto) {
        rabbitTemplate.convertAndSend(topicKey, refundQueueName, requestDto.toRefundQueueRequestDto())
    }
}
