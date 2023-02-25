package me.golf.kotlin.domain.bank.dto

import com.querydsl.core.annotations.QueryProjection
import me.golf.kotlin.domain.bank.model.BankName

data class BankAccountSummaryWithFinAccount

@QueryProjection
constructor(
    val id: Long,
    val bankName: BankName,
    val finAccount: String
) {

    fun toApiResponseDto(balance: String) =
        BankAccountSummaryResponseDto(this.id, balance, this.bankName.value)

    fun getMe() = this
}
