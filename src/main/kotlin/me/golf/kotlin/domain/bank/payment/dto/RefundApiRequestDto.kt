package me.golf.kotlin.domain.bank.payment.dto

import me.golf.kotlin.domain.bank.model.BankName
import java.math.BigDecimal

data class RefundApiRequestDto(
    val bankId: Long,
    val accountNumber: String,
    val fromName: String,
    val bankName: String,
    val transferMoney: String,
    val refundCause: String
) {
    fun toServiceDto(memberId: Long): RefundRequestDto {
        return RefundRequestDto(
            bankId = this.bankId,
            accountNumber = this.accountNumber,
            fromName = this.fromName,
            bankName = BankName.of(bankName),
            transferMoney = BigDecimal(this.transferMoney),
            refundCause = this.refundCause,
            memberId = memberId
        )
    }
}
