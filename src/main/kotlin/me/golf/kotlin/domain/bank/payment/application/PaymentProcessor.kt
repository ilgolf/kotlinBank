package me.golf.kotlin.domain.bank.payment.application

import me.golf.kotlin.domain.bank.history.application.PaymentHistoryService
import me.golf.kotlin.domain.bank.history.model.TransferStatus
import me.golf.kotlin.domain.bank.payment.client.PaymentApiClient
import me.golf.kotlin.domain.bank.payment.dto.PaymentMessageRequestDto
import me.golf.kotlin.domain.bank.payment.dto.RefundMessageRequestDto
import me.golf.kotlin.domain.bank.payment.dto.TransferResultDto
import me.golf.kotlin.global.common.PaymentMqPolicy
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Profile
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
@Profile("dev", "prd")
class PaymentProcessor(
    private val paymentApiClient: PaymentApiClient,
    private val paymentHistoryService: PaymentHistoryService
) {

    private val log = LoggerFactory.getLogger(PaymentProcessor::class.java)

    @RabbitListener(queues = [PaymentMqPolicy.PAYMENT_QUEUE])
    fun subscribePayment(requestDto: PaymentMessageRequestDto) {

        // request payment
        log.info("payment enqueue success : {}", requestDto.fromName)

        val resultMessage = paymentApiClient.pay(requestDto.finAccount, requestDto.transferMoney)

        val resultDto = TransferResultDto.createWithdrawal(
            requestDto.bankId,
            requestDto.fromName,
            requestDto.transferMoney,
            resultMessage,
            requestDto.clientId
        )

        // save history
        paymentHistoryService.save(resultDto)
    }

    @RabbitListener(queues = [PaymentMqPolicy.REFUND_QUEUE])
    fun subscribeRefund(requestDto: RefundMessageRequestDto) {
        // request refund
        log.info("refund enqueue success : {}", requestDto.fromName)

        val resultMessage = paymentApiClient.refund(requestDto)

        val resultDto = TransferResultDto(
            requestDto.bankId,
            requestDto.fromName,
            requestDto.transferMoney,
            TransferStatus.DEPOSIT,
            resultMessage,
            requestDto.clientId
        )

        // save history
        paymentHistoryService.save(resultDto)
    }
}