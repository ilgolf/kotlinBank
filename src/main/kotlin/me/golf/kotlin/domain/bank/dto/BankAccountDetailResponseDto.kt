package me.golf.kotlin.domain.bank.dto

import me.golf.kotlin.domain.bank.model.BankAccount

data class BankAccountDetailResponseDto(
    val name: String,
    val bankName: String,
    val number: String,
    val balance: String
) {

    constructor(bankAccount: BankAccount, balance: String): this(
        bankAccount.name,
        bankAccount.bankName.value,
        bankAccount.number,
        balance
    )
}
