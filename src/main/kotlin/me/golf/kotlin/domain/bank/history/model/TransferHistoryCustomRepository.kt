package me.golf.kotlin.domain.bank.history.model

import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface TransferHistoryCustomRepository {

    fun findHistoryByBankAccountId(bankAccountId: Long, memberId: Long, pageable: Pageable): Slice<HistorySummaryResponseDto>
}
