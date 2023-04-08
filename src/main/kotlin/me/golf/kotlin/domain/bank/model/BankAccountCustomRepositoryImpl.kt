package me.golf.kotlin.domain.bank.model

import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.dto.FinAccountAndBankIdDto
import me.golf.kotlin.domain.bank.dto.QBankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.dto.QFinAccountAndBankIdDto
import me.golf.kotlin.domain.bank.model.QBankAccount.*
import me.golf.kotlin.domain.bank.payment.dto.FinAccountDto
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

    override fun findFinAccountBy(bankId: Long): FinAccountAndBankIdDto? {
        return query.select(QFinAccountAndBankIdDto(
            bankAccount.id,
            bankAccount.finAccount))
            .from(bankAccount)
            .where(bankAccount.id.eq(bankId))
            .fetchOne()
    }
}