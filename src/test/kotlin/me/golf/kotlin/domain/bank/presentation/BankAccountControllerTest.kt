package me.golf.kotlin.domain.bank.presentation

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.bank.TestBankAccountUtils
import me.golf.kotlin.domain.bank.application.BankAccountCommandService
import me.golf.kotlin.domain.bank.application.BankAccountQueryService
import me.golf.kotlin.domain.bank.dto.*
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.history.application.PaymentHistoryService
import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import me.golf.kotlin.domain.bank.history.model.TransferStatus
import me.golf.kotlin.domain.bank.history.model.utils.TestTransferHistoryUtils
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
import java.time.LocalDateTime

class BankAccountControllerTest {

    private lateinit var customUserDetails: CustomUserDetails
    private lateinit var bankAccountController: BankAccountController
    private val bankAccountCommandService = mockk<BankAccountCommandService>()
    private val bankAccountQueryService = mockk<BankAccountQueryService>()
    private val paymentHistoryService = mockk<PaymentHistoryService>()

    @BeforeEach
    fun init() {
        customUserDetails = CustomUserDetails.of(GivenMember.toMember())
        bankAccountController = BankAccountController(bankAccountCommandService, bankAccountQueryService, paymentHistoryService)
    }

    @Test
    @DisplayName("계좌를 생성에 성공하면 200OK를 반환한다.")
    fun save() {
        // given
        val reqDto = BankAccountSaveApiRequestDto(
            name = TestBankAccountUtils.name,
            bankName = TestBankAccountUtils.bankName,
            password = TestBankAccountUtils.password,
            number = TestBankAccountUtils.number
        )

        val resDto = SimpleBankAccountIdResponseDto(1L)

        every { bankAccountCommandService.save(any()) } returns resDto

        // when
        val result: ResponseEntity<SimpleBankAccountIdResponseDto> = bankAccountController.save(reqDto, customUserDetails)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("계좌 생성 요청 시 지원하지 않는 은행이 존재하면 Exception을 발생시킨다.")
    fun saveFail() {
        // given
        val requestDto = BankAccountSaveApiRequestDto(
            name = "테스트입니다.",
            bankName = "스탠다드",
            password = "1231234",
            number = "31233210214"
        )

        // when
        val exception = catchException { bankAccountController.save(requestDto, customUserDetails) }


        // then
        assertThat(exception).isInstanceOf(BankAccountException.ConvertBankNameDeniedException::class.java)
    }

    @Test
    @DisplayName("계좌 정보 조회에 성공하면 200OK를 반환한다.")
    fun getAccountInfo() {
        // given
        val bankAccountId = 1L

        val summaryResDto = BankAccountDetailResponseDto(
            name = TestBankAccountUtils.name,
            bankName = TestBankAccountUtils.bankName,
            number = TestBankAccountUtils.number,
            balance = TestBankAccountUtils.balance
        )

        val historySummaryResDto = HistorySummaryResponseDto(
            transferMoney = TestTransferHistoryUtils.transferMoney,
            client = TestTransferHistoryUtils.client,
            transferStatus = TransferStatus.DEPOSIT,
            memberNickname = GivenMember.nickname,
            createdAt = LocalDateTime.now()
        )

        val resDtos = SliceImpl(arrayListOf(historySummaryResDto), PageRequest.of(0, 10), true)

        every { bankAccountQueryService.getBankAccountSummary(any(), any()) } returns summaryResDto
        every { paymentHistoryService.getHistories(any(), any(), any()) } returns SliceCustomResponse.of(resDtos)

        // when
        val result: ResponseEntity<BankAccountInfoResponseDto> = bankAccountController
            .getBankAccountSummary(bankAccountId, customUserDetails, PageRequest.of(0, 10))

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
    
    @Test
    @DisplayName("계좌 별칭 수정을 하고 200OK 응답한다.")
    fun updateNickname() {
        // given
        val bankAccountId = 1L
        val requestDto = BankAccountUpdateRequestDto("변경된 닉네임입니다.")

        every { bankAccountCommandService.update(any(), any(), any()) } returns Unit

        // when
        val result: ResponseEntity<Unit> = bankAccountController.update(requestDto, customUserDetails, bankAccountId)

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

    @Test
    @DisplayName("사용자가 갖고 있는 계좌를 모두 조회한다.")
    fun getBankAccountsByMemberId() {
        // given
        val responseDto = arrayListOf(
            BankAccountSummaryResponseDto(1L, TestBankAccountUtils.balance, TestBankAccountUtils.bankName)
        )

        every { bankAccountQueryService.getBankAccountsByMemberId(any()) } returns responseDto

        // when
        val result: ResponseEntity<List<BankAccountSummaryResponseDto>> =
            bankAccountController.getBankAccountsByMemberId(customUserDetails)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}