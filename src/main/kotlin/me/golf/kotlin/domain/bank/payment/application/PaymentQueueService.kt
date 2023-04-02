package me.golf.kotlin.domain.bank.payment.application

import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.payment.dto.RefundRequestDto
import me.golf.kotlin.domain.bank.payment.dto.TransferRequestDto
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("dev", "prd")
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
