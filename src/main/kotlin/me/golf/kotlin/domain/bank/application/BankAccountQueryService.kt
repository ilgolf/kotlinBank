package me.golf.kotlin.domain.bank.application

import me.golf.kotlin.domain.bank.BalanceResponseDto
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryResponseDto
import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import me.golf.kotlin.global.common.SliceCustomResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BankAccountQueryService {

    fun getBankAccountSummary(bankAccountId: Long, memberId: Long): BankAccountSummaryResponseDto {
        TODO("Not yet implemented")
    }

    fun getHistory(bankAccountId: Long, memberId: Long): SliceCustomResponse<HistorySummaryResponseDto> {
        TODO("Not yet implemented")
    }

    fun getBalance(bankAccountId: Long, memberId: Long): BalanceResponseDto {
        TODO("Not yet implemented")
    }
}
