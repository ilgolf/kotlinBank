package me.golf.kotlin.domain.bank.payment.dto

import me.golf.kotlin.domain.bank.model.BankName
import java.math.BigDecimal

data class RefundMessageRequestDto(
    val bankId: Long,
    val accountNumber: String,
    val transferMoney: BigDecimal,
    val refundCause: String,
    val fromName: String,
    val bankName: BankName,
    val clientId: Long
)
