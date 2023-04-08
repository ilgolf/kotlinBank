package me.golf.kotlin.domain.bank.model

import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.dto.FinAccountAndBankIdDto
import me.golf.kotlin.domain.bank.payment.dto.FinAccountDto

interface BankAccountCustomRepository {

    fun findSummaryByMemberId(memberId: Long): List<BankAccountSummaryWithFinAccount>

    fun findFinAccountBy(bankId: Long): FinAccountAndBankIdDto?
}
