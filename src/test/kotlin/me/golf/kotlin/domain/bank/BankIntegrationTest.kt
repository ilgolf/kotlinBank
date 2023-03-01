package me.golf.kotlin.domain.bank

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.kotlin.commonutil.IntegrationTest
import me.golf.kotlin.domain.bank.dto.BankAccountSaveApiRequestDto
import me.golf.kotlin.domain.bank.dto.BankAccountUpdateRequestDto
import me.golf.kotlin.domain.bank.history.model.TransferHistoryRepository
import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.model.BankName
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
class BankIntegrationTest

@Autowired
constructor(
    private val memberRepository: MemberRepository,
    private val objectMapper: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService,
    private val bankAccountRepository: BankAccountRepository,
    private val transferHistoryRepository: TransferHistoryRepository,
): IntegrationTest() {

    private lateinit var member: Member
    private lateinit var accessToken: String
    private lateinit var bankAccount: BankAccount

    @BeforeEach
    fun init() {
        member = memberRepository.save(GivenMember.toMember())

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authentication = UsernamePasswordAuthenticationToken(userDetails, "")
        val tokenBaseDto = tokenProvider.createToken(member.id, authentication)
        accessToken = tokenBaseDto.accessToken

        bankAccount = bankAccountRepository.save(TestBankAccountUtils.createBankAccountBy(member.id))

        transferHistoryRepository.findHistoryByBankAccountId(
            bankAccountId = bankAccount.id,
            memberId = member.id,
            pageable = PageRequest.of(0, 10)
        )
    }

    @Test
    @DisplayName("계좌 생성 통합 테스트")
    fun createAccount() {
        // given
        val dto = BankAccountSaveApiRequestDto(
            name = "테스트용 이름2",
            bankName = BankName.WOORI.value,
            password = "1098",
            number = "32122-21323-12314"
        )

        val body = objectMapper.writeValueAsString(dto)

        // when
        val resultActions = mockMvc.perform(
            post("/api/v2/bank-accounts").content(body)
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("단일 계좌 상세 조회 통합 테스트")
    fun getBankAccountSummary() {
        // given
        val bankAccountId = bankAccount.id

        // when
        val resultActions = mockMvc.perform(
            get("/api/v2/bank-accounts/${bankAccountId}")
                .header("Authorization", "Bearer $accessToken")
                .param("page", "0")
                .param("size", "10")
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("회원이 갖고 있는 계좌를 조회 통합 테스트")
    fun getBankAccountsByMemberId() {
        // given

        // when
        val resultActions = mockMvc.perform(
            get("/api/v2/bank-accounts")
                .header("Authorization", "Bearer $accessToken")
                .param("page", "0")
                .param("size", "10")
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("회원의 계좌 잔액을 최신으로 업데이트한다.")
    fun updateBalance() {
        // given

        // when
        val resultActions = mockMvc.perform(
            patch("/api/v2/bank-accounts/balance/${bankAccount.id}")
                .header("Authorization", "Bearer $accessToken")
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("회원의 계좌를 수정한다.")
    fun update() {
        // given
        val requestDto = BankAccountUpdateRequestDto("테스트 업데이트 용")
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val resultActions = mockMvc.perform(
            patch("/api/v2/bank-accounts/${bankAccount.id}")
                .header("Authorization", "Bearer $accessToken")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }
    
    @Test
    @DisplayName("계좌정보를 삭제한다.")
    fun deleteTest() {
        // given

        // when
        val resultActions = mockMvc.perform(
            delete("/api/v2/bank-accounts/${bankAccount.id}")
                .header("Authorization", "Bearer $accessToken")
        )

        // then
        resultActions.andExpect(status().isNoContent)
            .andDo(print())
    }
}