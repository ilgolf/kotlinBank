package me.golf.kotlin.domain.bank

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.bank.application.BankAccountCommandService
import me.golf.kotlin.domain.bank.application.BankAccountQueryService
import me.golf.kotlin.domain.bank.dto.*
import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import me.golf.kotlin.domain.bank.history.model.utils.TestTransferHistoryUtils
import me.golf.kotlin.domain.bank.presentation.BankAccountController
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.common.SliceCustomResponse
import me.golf.kotlin.global.security.CustomUserDetails
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.SliceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal
import java.time.LocalDate

class BankAccountControllerTest {

    private lateinit var customUserDetails: CustomUserDetails
    private lateinit var bankAccountController: BankAccountController
    private val bankAccountCommandService = mockk<BankAccountCommandService>()
    private val bankAccountQueryService = mockk<BankAccountQueryService>()

    @BeforeEach
    fun init() {
        customUserDetails = CustomUserDetails.of(GivenMember.toMember())
        bankAccountController = BankAccountController(bankAccountCommandService, bankAccountQueryService)
    }

    @Test
    @DisplayName("계좌를 생성에 성공하면 200OK를 반환한다.")
    fun save() {
        // given
        val reqDto = BankAccountSaveApiRequestDto(
            name = GivenBankAccount.name,
            bankName = GivenBankAccount.bankName,
            password = GivenBankAccount.password,
            number = GivenBankAccount.number
        )

        val resDto = SimpleBankAccountIdResponseDto(1L)

        every { bankAccountCommandService.save(any()) } returns resDto

        // when
        val result: ResponseEntity<SimpleBankAccountIdResponseDto> = bankAccountController.save(reqDto, customUserDetails)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("계좌 정보 조회에 성공하면 200OK를 반환한다.")
    fun getAccountInfo() {
        // given
        val bankAccountId = 1L

        val summaryResDto = BankAccountSummaryResponseDto(
            name = GivenBankAccount.name,
            bankName = GivenBankAccount.bankName,
            number = GivenBankAccount.number
        )

        val historySummaryResDto = HistorySummaryResponseDto(
            transferMoney = TestTransferHistoryUtils.transferMoney,
            depositor = TestTransferHistoryUtils.depositor,
            memberNickname = GivenMember.nickname,
            createdAt = LocalDate.now()
        )

        val resDtos = SliceImpl(arrayListOf(historySummaryResDto), PageRequest.of(0, 10), true)

        every { bankAccountQueryService.getBankAccountSummary(any(), any()) } returns summaryResDto
        every { bankAccountQueryService.getHistory(any(), any()) } returns SliceCustomResponse.of(resDtos)

        // when
        val result: ResponseEntity<BankAccountInfoResponseDto> = bankAccountController.getBankAccountSummary(bankAccountId, customUserDetails)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("계좌 잔액을 조회에 성공하면 200OK")
    fun getBalance() {
        // given
        val bankAccountId = 1L
        val resDto = BalanceResponseDto(BigDecimal(60000))

        every { bankAccountQueryService.getBalance(any(), any()) } returns resDto

        // when
        val result: ResponseEntity<BalanceResponseDto> = bankAccountController.getBalance(bankAccountId, customUserDetails)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
    
    @Test
    @DisplayName("계좌 별칭 수정을 하고 200OK 응답한다.")
    fun updateNickname() {
        // given
        val bankAccountId = 1L
        val requestDto = BankAccountUpdateRequestDto("변경된 닉네임입니다.")

        every { bankAccountCommandService.update(any(), any()) } returns Unit

        // when
        val result: ResponseEntity<Unit> = bankAccountController.update(requestDto, bankAccountId)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("계좌를 삭제하고 200OK 응답한다.")
    fun delete() {
        // given
        val bankAccountId = 1L

        every { bankAccountCommandService.delete(any(), any()) } returns Unit

        // when
        val result: ResponseEntity<Unit> = bankAccountController.delete(bankAccountId, customUserDetails)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }
}