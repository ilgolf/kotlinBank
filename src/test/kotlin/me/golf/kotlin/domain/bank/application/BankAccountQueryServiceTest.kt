package me.golf.kotlin.domain.bank.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.bank.TestBankAccountUtils
import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryResponseDto
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.model.BankAccountRedisRepository
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.model.BankName
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class BankAccountQueryServiceTest {

    private lateinit var bankAccountQueryService: BankAccountQueryService
    private val bankAccountRepository = mockk<BankAccountRepository>()
    private val bankAccountApiClient = mockk<BankAccountApiClient>()
    private val bankAccountRedisRepository = mockk<BankAccountRedisRepository>()

    @BeforeEach
    fun init() {
        bankAccountQueryService = BankAccountQueryService(bankAccountRepository, bankAccountApiClient,
            bankAccountRedisRepository)
    }

    @Test
    @DisplayName("사용자가 보유한 모든 계좌정보를 조회한다.")
    fun getBankAccountsByMemberId() {
        // given
        val responseDtos = arrayListOf(
            BankAccountSummaryResponseDto(1L, "45600", BankName.KB.value),
            BankAccountSummaryResponseDto(2L, "67900", BankName.SIN_HAN.value)
        )

        val dtos = arrayListOf(
            BankAccountSummaryWithFinAccount(1L, BankName.KB, "1111"),
            BankAccountSummaryWithFinAccount(2L, BankName.SIN_HAN, "2222")
        )

        val finAccountToBalanceMap = mapOf("1111" to "45600", "2222" to "67900")

        every { bankAccountRepository.findAllByMemberId(any()) } returns dtos
        every { bankAccountRedisRepository.findBalancesByFinAccount(any()) } returns finAccountToBalanceMap

        // when
        val result = bankAccountQueryService.getBankAccountsByMemberId(1L)

        // then
        assertAll(
            { assertThat(result[0]).isEqualTo(responseDtos[0]) },
            { assertThat(result[1]).isEqualTo(responseDtos[1]) }
        )
    }

    @Test
    @DisplayName("memberId 기준으로 잔액 조회 현황을 최신화 시켜준다.")
    fun updateBalanceByMemberId() {
        // given
        val memberId = 1L
        val balances = arrayListOf("45600", "67900")
        val dtos = arrayListOf(
            BankAccountSummaryWithFinAccount(1L, BankName.KB, "1111"),
            BankAccountSummaryWithFinAccount(2L, BankName.SIN_HAN, "2222")
        )

        every { bankAccountRepository.findAllByMemberId(any()) } returns dtos
        every { bankAccountApiClient.getBalances(any()) } returns balances

        // when
        val result = bankAccountQueryService.updateBalanceByMemberId(memberId)

        // then
        assertAll(
            { assertThat(result[0]).isEqualTo("45600") },
            { assertThat(result[1]).isEqualTo("67900") }
        )
    }

    @Test
    @DisplayName("계좌 상세 정보를 조회한다.")
    fun getBalance() {
        // given
        every { bankAccountRepository.findByIdAndMemberId(any(), any()) } returns TestBankAccountUtils.mockBankAccount()
        every { bankAccountRedisRepository.findBalanceByFinAccount(any()) } returns TestBankAccountUtils.balance

        // when
        val accountSummary = bankAccountQueryService.getBankAccountSummary(1L, 1L)

        // then
        assertAll(
            { assertThat(accountSummary.balance).isEqualTo(TestBankAccountUtils.balance) },
            { assertThat(accountSummary.bankName).isEqualTo(TestBankAccountUtils.bankName) },
            { assertThat(accountSummary.name).isEqualTo(TestBankAccountUtils.name) },
            { assertThat(accountSummary.number).isEqualTo(TestBankAccountUtils.number) }
        )
    }

    @Test
    @DisplayName("계좌 상세 정보를 DB 조회에 실패하면 예외가 발생한다.")
    fun getBalanceFailByNotFound() {
        // given
        every { bankAccountRepository.findByIdAndMemberId(any(), any()) } returns null

        // when
        val exception = catchException { bankAccountQueryService.getBankAccountSummary(1L, 1L) }

        // then
        assertThat(exception).isInstanceOf(BankAccountException.NotFoundException::class.java)
    }

    @Test
    @DisplayName("잔액 정보를 받아오지 못하면 초기엔 ????로 보여진다.")
    fun getBalanceCaseBalanceNotFound() {
        // given
        every { bankAccountRepository.findByIdAndMemberId(any(), any()) } returns TestBankAccountUtils.mockBankAccount()
        every { bankAccountRedisRepository.findBalanceByFinAccount(any()) } returns null

        // when
        val result = bankAccountQueryService.getBankAccountSummary(1L, 1L)

        // then
        assertThat(result.balance).isEqualTo("????")
    }
}