package me.golf.kotlin.domain.bank.history.dto

import com.querydsl.core.annotations.QueryProjection
import me.golf.kotlin.domain.bank.history.model.TransferStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class HistorySummaryResponseDto

@QueryProjection
constructor(
    val transferMoney: BigDecimal,
    val client: String,
    val transferStatus: TransferStatus,
    val memberNickname: String,
    val createdAt: LocalDateTime
)
