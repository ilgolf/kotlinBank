package me.golf.kotlin.domain.bank

import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.domain.bank.model.BankName

object TestBankAccountUtils {

    const val number = "1234-1234567-1234-56"
    const val password = "1234"
    const val memberId = 1L
    const val bankName = "신한은행"
    const val pk = 1L
    const val name = "별칭입니다."
    const val balance = "45600"

    fun mockBankAccount(): BankAccount {
        return BankAccount(
            number = number,
            password = password,
            registerNumber = "23912893",
            finAccount = "8038129893",
            memberId = memberId,
            bankName = BankName.of(bankName),
            name = name
        )
    }

    fun createBankAccountBy(memberId: Long): BankAccount {
        return BankAccount(
            number = number,
            password = password,
            registerNumber = "23912893",
            finAccount = "8038129893",
            memberId = memberId,
            bankName = BankName.of(bankName),
            name = name
        )
    }
}
