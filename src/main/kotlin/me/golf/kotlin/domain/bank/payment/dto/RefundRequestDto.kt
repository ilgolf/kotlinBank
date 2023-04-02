package me.golf.kotlin.domain.bank.payment.dto

import me.golf.kotlin.domain.bank.model.BankName
import java.math.BigDecimal

data class RefundRequestDto(
    val bankId: Long,
    val accountNumber: String,
    val fromName: String,
    val bankName: BankName,
    val transferMoney: BigDecimal,
    val refundCause: String,
    val memberId: Long
) {
    fun toRefundQueueRequestDto(): RefundMessageRequestDto {
        return RefundMessageRequestDto(
            this.bankId,
            this.accountNumber,
            this.transferMoney,
            this.refundCause,
            this.fromName,
            this.bankName,
            this.memberId
        )
    }
}
