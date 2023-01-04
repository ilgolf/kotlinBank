package me.golf.kotlin.domain.bank.history.model.utils

import me.golf.kotlin.domain.bank.GivenBankAccount
import me.golf.kotlin.domain.bank.history.model.TransferHistory
import java.math.BigDecimal

object TestTransferHistoryUtils {

    val transferMoney = BigDecimal.valueOf(5000)
    const val depositor = 1L

    fun toEntity(): TransferHistory {
        return TransferHistory(
            transferMoney,
            depositor,
            1L,
            GivenBankAccount.mockBankAccount(),
        )
    }
}