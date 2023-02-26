package me.golf.kotlin.domain.bank.dto

import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.domain.bank.model.BankName

data class BankAccountSaveRequestDto(
    val name: String,
    val bankName: BankName,
    val password: String,
    val number: String,
    val memberId: Long
) {

    fun toEntity(registerNumber: String, finAccount: String) =
        BankAccount(
            number = number,
            password = password,
            registerNumber = registerNumber,
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
