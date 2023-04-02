package me.golf.kotlin.domain.bank.payment.dto

import java.math.BigDecimal

data class TransferRequestDto(
    val bankId: Long,
    val fromName: String,
    val transferMoney: BigDecimal,
    val memberId: Long
) {

    fun toPaymentMessageRequestDto(finAccount: String): PaymentMessageRequestDto {
        return PaymentMessageRequestDto(bankId, finAccount, this.transferMoney, this.fromName, this.memberId)
    }
}
