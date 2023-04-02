package me.golf.kotlin.domain.bank.payment.dto

import java.math.BigDecimal

data class PaymentMessageRequestDto(
    val bankId: Long,
    val finAccount: String,
    val transferMoney: BigDecimal,
    val fromName: String,
    val clientId: Long
)
