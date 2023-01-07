package me.golf.kotlin.domain.bank.history.dto

import java.math.BigDecimal
import java.time.LocalDate

data class HistorySummaryResponseDto(
    val transferMoney: BigDecimal,
    val depositor: Long,
    val memberNickname: String,
    val createdAt: LocalDate
)
