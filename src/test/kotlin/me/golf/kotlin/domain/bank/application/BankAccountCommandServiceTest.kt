package me.golf.kotlin.domain.bank.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.bank.TestBankAccountUtils
import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.BankAccountSaveRequestDto
import me.golf.kotlin.domain.bank.dto.SimpleBankAccountIdResponseDto
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.member.application.MemberQueryService
import me.golf.kotlin.domain.member.dto.response.MemberApiDetailDto
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.domain.member.util.TestPasswordEncoder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class BankAccountCommandServiceTest {

    lateinit var bankAccountCommandService:BankAccountCommandService
    private val bankAccountRepository = mockk<BankAccountRepository>()
    private val bankAccountApiClient = mockk<BankAccountApiClient>()
    private val passwordEncoder: TestPasswordEncoder = TestPasswordEncoder.init()
    private val memberQueryService = mockk<MemberQueryService>()
    private val bankAccountLockService = mockk<BankAccountLockService>(relaxed = true)

    @BeforeEach
    fun init() {
        bankAccountCommandService = BankAccountCommandService(bankAccountRepository, passwordEncoder,
            bankAccountApiClient, memberQueryService, bankAccountLockService)
    }

    @Test
    @DisplayName("사용자의 계좌 정보를 저장한다.")
    fun save() {
        // given
        val requestDto = BankAccountSaveRequestDto.testInitializer(TestBankAccountUtils.mockBankAccount(), TestBankAccountUtils.memberId)
        val finTechAccount = "20191124000000249"
        val bankAccount = TestBankAccountUtils.mockBankAccount()
        bankAccount.id = 1L

        every { bankAccountRepository.existsByName(any()) } returns false
        every { bankAccountRepository.existsByNumber(any()) } returns false
        every { bankAccountApiClient.getFinAccountConnection(any()) } returns finTechAccount
        every { bankAccountRepository.save(any()) } returns bankAccount
        every { memberQueryService.getDetail(any()) } returns MemberApiDetailDto.of(GivenMember.toMember())
        every { bankAccountLockService.tryLock(any()) } returns true

        // when
        val responseDto: SimpleBankAccountIdResponseDto = bankAccountCommandService.save(requestDto)

        // then
        assertThat(responseDto.bankAccountId).isEqualTo(1L)
    }

    @Test
    @DisplayName("finAccount 정보를 받아오지 못하면 에러가 발생한다.")
    fun saveFailWithFinAccount() {
        // given
        val requestDto = BankAccountSaveRequestDto.testInitializer(TestBankAccountUtils.mockBankAccount(), TestBankAccountUtils.memberId)
        val bankAccount = TestBankAccountUtils.mockBankAccount()
        bankAccount.id = 1L
        val member = MemberApiDetailDto.of(GivenMember.toMember())

        every { bankAccountRepository.existsByNumber(any()) } returns false
        every { bankAccountRepository.existsByName(any()) } returns false
        every { memberQueryService.getDetail(any()) } returns member
        every { bankAccountApiClient.getFinAccountConnection(any()) } throws BankAccountException.FinAccountNotFoundException()
        every { bankAccountLockService.tryLock(any()) } returns true

        // when
        val exception = catchException { bankAccountCommandService.save(requestDto) }

        // then
        assertThat(exception).isInstanceOf(BankAccountException.FinAccountNotFoundException::class.java)
    }

    @Test
    @DisplayName("회원 계좌 이름을 수정할 수 있다.")
    fun update() {
        // given
        val requestNickname = "update_nickname"
        val memberId = 1L
        val bankAccountId = 1L
        val bankAccount = TestBankAccountUtils.mockBankAccount()

        every { bankAccountRepository.findByIdAndMemberId(bankAccountId, memberId) } returns bankAccount

        // when
        bankAccountCommandService.update(requestNickname, bankAccountId, memberId)

        // then
        assertThat(bankAccount.name).isEqualTo(requestNickname)
    }

    @Test
    @DisplayName("수정 요청 시 bankAccount를 찾지 못하면 예외가 발생한다.")
    fun updateFail() {
        // given
        val memberId = 1L
        val bankAccountId = 1L
        val requestNickname = "updateNickname"

        every { bankAccountRepository.findByIdAndMemberId(any(), any()) } returns null

        // when
        val exception =
            catchException { bankAccountCommandService.update(requestNickname, bankAccountId, memberId) }

        // then
        assertThat(exception).isInstanceOf(BankAccountException.NotFoundException::class.java)
    }

    @Test
    @DisplayName("삭제 요청을 하면 deleted가 true가 된다.")
    fun delete() {
        // given
        val memberId = 1L
        val bankAccountId = 1L
        val bankAccount = TestBankAccountUtils.mockBankAccount()

        every { bankAccountRepository.findByIdAndMemberId(any(), any()) } returns bankAccount

        // when
        bankAccountCommandService.delete(bankAccountId, memberId)

        // then
        assertThat(bankAccount.deleted).isEqualTo(true)
    }

    @Test
    @DisplayName("삭제 요청 시 bankAccount를 찾지 못하면 예외가 발생한다.")
    fun deleteFail() {
        // given
        val memberId = 1L
        val bankAccountId = 1L

        every { bankAccountRepository.findByIdAndMemberId(any(), any()) } returns null

        // when
        val exception = catchException { bankAccountCommandService.delete(bankAccountId, memberId) }

        // then
        assertThat(exception).isInstanceOf(BankAccountException.NotFoundException::class.java)
    }
}