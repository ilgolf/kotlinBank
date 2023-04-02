package me.golf.kotlin.domain.bank.payment.dto

import me.golf.kotlin.domain.bank.history.model.TransferHistory
import me.golf.kotlin.domain.bank.history.model.TransferStatus
import java.math.BigDecimal

data class TransferResultDto(
    val bankId: Long,
    val fromName: String,
    val transferMoney: BigDecimal,
    val transferStatus: TransferStatus,
    val resultMessage: String,
    val clientId: Long
) {

    companion object {
        fun createWithdrawal(bankId: Long, name: String, transferMoney: BigDecimal, resultMessage: String, clientId: Long) =
            TransferResultDto(
                bankId = bankId,
                fromName = name,
                transferMoney = transferMoney,
                transferStatus = TransferStatus.WITHDRAWAL,
                resultMessage = resultMessage,
                clientId = clientId
            )

        fun createDeposit(bankId: Long, name: String, transferMoney: BigDecimal, resultMessage: String, clientId: Long) =
            TransferResultDto(
                bankId = bankId,
                fromName = name,
                transferMoney = transferMoney,
                transferStatus = TransferStatus.DEPOSIT,
                resultMessage = resultMessage,
                clientId = clientId
            )
    }

    fun toTransferHistory(): TransferHistory {
        return TransferHistory(
            transferMoney = transferMoney,
            client = this.clientId,
            bankId = this.bankId,
            transferStatus = transferStatus,
            resultMessage = resultMessage
        )
    }
}
