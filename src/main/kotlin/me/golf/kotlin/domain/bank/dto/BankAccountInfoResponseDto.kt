package me.golf.kotlin.domain.bank.dto

import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import me.golf.kotlin.global.common.SliceCustomResponse

data class BankAccountInfoResponseDto (
    val bankAccountDetailResponseDto: BankAccountDetailResponseDto,
    val historySummaryResponseDto: SliceCustomResponse<HistorySummaryResponseDto>
)