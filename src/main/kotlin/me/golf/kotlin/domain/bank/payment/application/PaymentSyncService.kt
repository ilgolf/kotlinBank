package me.golf.kotlin.domain.bank.payment.application

import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.history.application.PaymentHistoryService
import me.golf.kotlin.domain.bank.history.model.TransferStatus
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.payment.client.PaymentApiClient
import me.golf.kotlin.domain.bank.payment.dto.RefundRequestDto
import me.golf.kotlin.domain.bank.payment.dto.TransferRequestDto
import me.golf.kotlin.domain.bank.payment.dto.TransferResultDto
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("dev", "prd")
class PaymentSyncService(
    private val paymentApiClient: PaymentApiClient,
    private val bankAccountRepository: BankAccountRepository,
    private val paymentHistoryService: PaymentHistoryService
): PaymentService {

    override fun pay(requestDto: TransferRequestDto) {
        val finAccountDto = bankAccountRepository.findFinAccountBy(requestDto.bankId)
            ?: throw BankAccountException.NotFoundException()

        val result = paymentApiClient.pay(finAccountDto.finAccount, requestDto.transferMoney)

        val resultDto = TransferResultDto.createWithdrawal(
            requestDto.bankId,
            requestDto.fromName,
            requestDto.transferMoney,
            result,
            requestDto.memberId
        )

        paymentHistoryService.save(resultDto)
    }

    override fun refund(requestDto: RefundRequestDto) {
        val resultMessage = paymentApiClient.refund(requestDto.toRefundQueueRequestDto())

        val resultDto = TransferResultDto(
            requestDto.bankId,
            requestDto.fromName,
            requestDto.transferMoney,
            TransferStatus.DEPOSIT,
            resultMessage,
            requestDto.memberId
        )

        paymentHistoryService.save(resultDto)
    }
}