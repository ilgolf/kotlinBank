package me.golf.kotlin.domain.bank.dto

import me.golf.kotlin.domain.bank.model.BankAccount

data class BankAccountSaveRequestDto(
    val name: String,
    val bankName: String,
    val password: String,
    val number: String,
    val memberId: Long
) {

    fun toEntity(finAccount: String) =
        BankAccount(
            number = number,
            password = password,
            finAccount = finAccount,
            memberId = memberId,
            bankName = bankName,
            name = name
        )

    companion object {
        fun testInitializer(bankAccount: BankAccount, memberId: Long) =
            BankAccountSaveRequestDto(
                name = bankAccount.name,
                bankName = bankAccount.bankName,
                password = bankAccount.password,
                number = bankAccount.number,
                memberId = memberId
            )
    }
}
