package me.golf.kotlin.domain.bank.model

import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount

interface BankAccountCustomRepository {

    fun findAllByMemberId(memberId: Long): List<BankAccountSummaryWithFinAccount>
}
