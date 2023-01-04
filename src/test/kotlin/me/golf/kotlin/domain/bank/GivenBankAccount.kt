package me.golf.kotlin.domain.bank

import me.golf.kotlin.domain.bank.model.BankAccount
import java.math.BigDecimal

object GivenBankAccount {

    const val number = "1234-1234567-1234-56"
    const val password = "1234"
    const val memberId = 1L
    const val bankName = "신한"
    const val pk = 1L
    const val name = "별칭입니다."
    val balance = BigDecimal(8000)

    fun mockBankAccount(): BankAccount {
        return BankAccount(
            number = number,
            password = password,
            memberId = memberId,
            bankName = bankName,
            balance = balance,
            name = name
        )
    }
}
