package me.golf.kotlin.domain.bank

import me.golf.kotlin.domain.bank.model.BankAccount
import java.math.BigDecimal

object GivenBankAccount {

    private const val number = "1234-1234567-1234-56"
    private const val password = "1234"
    private const val memberId = 1L
    private const val bankName = "신한"
    private const val pk = 1L
    private const val name = "별칭입니다."
    private val balance = BigDecimal(8000)

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
