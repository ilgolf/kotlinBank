package me.golf.kotlin.domain.bank.model

import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount

interface BankAccountCustomRepository {

    fun findSummaryByMemberId(memberId: Long): List<BankAccountSummaryWithFinAccount>
}
