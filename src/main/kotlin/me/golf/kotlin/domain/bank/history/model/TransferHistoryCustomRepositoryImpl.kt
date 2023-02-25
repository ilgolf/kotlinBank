package me.golf.kotlin.domain.bank.history.model

import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import me.golf.kotlin.domain.bank.history.dto.QHistorySummaryResponseDto
import me.golf.kotlin.domain.bank.history.model.QTransferHistory.transferHistory
import me.golf.kotlin.domain.bank.model.QBankAccount.bankAccount
import me.golf.kotlin.domain.member.model.QMember.member
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository

@Repository
class TransferHistoryCustomRepositoryImpl(
    private val query: JPAQueryFactory
) : TransferHistoryCustomRepository {

    override fun findHistoryByBankAccountId(bankAccountId: Long, memberId: Long, pageable: Pageable): Slice<HistorySummaryResponseDto> {
        val summaryResponseDtos = query.select(
            QHistorySummaryResponseDto(
                transferHistory.transferMoney,
                bankAccount.name,
                transferHistory.transferStatus,
                member.nickname,
                transferHistory.createdAt
            ))
            .from(transferHistory)
            .innerJoin(bankAccount).on(bankAccount.id.eq(transferHistory.bankId))
            .innerJoin(member).on(member.id.eq(transferHistory.client))
            .where(transferHistory.client.eq(memberId))
            .where(transferHistory.bankId.eq(bankAccountId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong() + 1)
            .fetch()

        if (summaryResponseDtos.size == pageable.pageSize + 1) {
            summaryResponseDtos.removeAt(summaryResponseDtos.size - 1)
            return SliceImpl(summaryResponseDtos, pageable, true)
        }

        return SliceImpl(summaryResponseDtos, pageable, false)
    }
}