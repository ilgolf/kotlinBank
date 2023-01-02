package me.golf.kotlin.domain.bank.model

import me.golf.kotlin.domain.bank.GivenBankAccount
import me.golf.kotlin.domain.member.util.TestPasswordEncoder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.security.crypto.password.PasswordEncoder

internal class BankAccountTest {

    private lateinit var encoder: PasswordEncoder
    private lateinit var bankAccount: BankAccount

    @BeforeEach
    fun init() {
        encoder = TestPasswordEncoder.init()
        bankAccount = GivenBankAccount.mockBankAccount()
    }

    @Test
    fun encodePassword() {
        // given

        // when

        // then

    }

    @Test
    fun matchPassword() {
    }

    @Test
    fun updateAccountName() {
    }
}