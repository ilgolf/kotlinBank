package me.golf.kotlin.domain.bank.model

import me.golf.kotlin.domain.bank.GivenBankAccount
import me.golf.kotlin.domain.bank.error.TooMuchTransferAmountException
import me.golf.kotlin.domain.member.util.TestPasswordEncoder
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
import java.util.TooManyListenersException

internal class BankAccountTest {

    private lateinit var encoder: PasswordEncoder
    private lateinit var bankAccount: BankAccount

    @BeforeEach
    fun init() {
        encoder = TestPasswordEncoder.init()
        bankAccount = GivenBankAccount.mockBankAccount()
    }

    @Test
    @DisplayName("계좌번호의 비밀번호를 인코딩한다.")
    fun encodePassword() {
        // when
        val encodedAccount = bankAccount.encodePassword(encoder)

        // then
        assertThat(encodedAccount.password).isNotEqualTo(GivenBankAccount.password)
    }

    @Test
    @DisplayName("계좌번호의 비밀번호 일치 여부를 판단한다.")
    fun matchPassword() {
        // given
        val encodedAccount = bankAccount.encodePassword(encoder)

        // when
        val isMatch = encodedAccount.matchPassword(encoder, GivenBankAccount.password)

        // then
        assertThat(isMatch).isTrue
    }

    @Test
    @DisplayName("계좌번호 별칭을 수정할 수 있다.")
    fun updateAccountName() {
        // given
        val updateName = "goods"

        // when
        val updateAccount = bankAccount.updateAccountName(updateName)

        // then
        assertThat(updateAccount.name).isEqualTo("goods")
    }

    @Test
    @DisplayName("송금할 수 있는 금액이 아니면 예외를 발생시킨다.")
    fun validateTransferPossibility() {
        // given
        val transferAmount = BigDecimal(9000)

        // when
        val exception = catchException { bankAccount.validTransferPossibility(transferAmount) }

        // then
        assertThat(exception).isInstanceOf(TooMuchTransferAmountException::class.java)
    }
}