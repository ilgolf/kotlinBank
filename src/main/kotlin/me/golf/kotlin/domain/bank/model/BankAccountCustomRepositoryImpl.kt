package me.golf.kotlin.domain.bank.model

import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.dto.QBankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.model.QBankAccount.*
import org.springframework.transaction.annotation.Transactional

open class BankAccountCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : BankAccountCustomRepository {

    @Transactional(readOnly = true)
    override fun findSummaryByMemberId(memberId: Long): List<BankAccountSummaryWithFinAccount> {
        return query.select(
            QBankAccountSummaryWithFinAccount(
                bankAccount.id,
                bankAccount.bankName,
                bankAccount.finAccount
            )
        )
            .from(bankAccount)
            .where(bankAccount.memberId.eq(memberId))
            .fetch()
    }

    @Transactional
    override fun updateFinAccountAndRegisterNumber(finAccount: String, registerNumber: String, bankAccountId: Long) {
        query.update(bankAccount)
            .set(bankAccount.finAccount, finAccount)
            .set(bankAccount.registerNumber, registerNumber)
            .where(bankAccount.id.eq(bankAccountId))
            .execute()
    }
}