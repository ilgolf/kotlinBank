package me.golf.kotlin.domain.bank.model

import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.dto.QBankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.model.QBankAccount.*

class BankAccountCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : BankAccountCustomRepository {

    override fun findSummaryByMemberId(memberId: Long): List<BankAccountSummaryWithFinAccount> {
        return query.select(
            QBankAccountSummaryWithFinAccount(
                bankAccount.id,
                bankAccount.bankName,
                bankAccount.finAccount
            ))
            .from(bankAccount)
            .where(bankAccount.memberId.eq(memberId))
            .fetch()
    }
}