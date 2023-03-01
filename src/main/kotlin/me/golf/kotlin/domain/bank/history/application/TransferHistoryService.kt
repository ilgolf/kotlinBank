package me.golf.kotlin.domain.bank.history.application

import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import me.golf.kotlin.domain.bank.history.model.TransferHistoryRepository
import me.golf.kotlin.global.common.SliceCustomResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service

@Service
class TransferHistoryService(
    private val transferHistoryRepository: TransferHistoryRepository
) {


    fun getHistories(memberId: Long, bankAccountId: Long, pageable: Pageable): SliceCustomResponse<HistorySummaryResponseDto> {
        val responseDto: Slice<HistorySummaryResponseDto> =
            transferHistoryRepository.findHistoryByBankAccountId(bankAccountId, memberId, pageable)

        return SliceCustomResponse.of(responseDto)
    }
}
