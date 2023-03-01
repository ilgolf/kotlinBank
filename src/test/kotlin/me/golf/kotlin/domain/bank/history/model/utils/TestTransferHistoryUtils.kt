package me.golf.kotlin.domain.bank.history.model.utils

import me.golf.kotlin.domain.bank.history.model.TransferHistory
import me.golf.kotlin.domain.bank.history.model.TransferStatus
import java.math.BigDecimal

object TestTransferHistoryUtils {

    val transferMoney: BigDecimal = BigDecimal.valueOf(5000)
    const val client = "테스트용 클라이언트"

    fun toEntity(): TransferHistory {
        return TransferHistory(
            transferMoney,
            1L,
            1L,
            TransferStatus.WITHDRAWAL
        )
    }

    fun toEntity(bankId: Long, memberId: Long): TransferHistory {
        return TransferHistory(
            transferMoney,
            memberId,
            bankId,
            TransferStatus.WITHDRAWAL
        )
    }
}