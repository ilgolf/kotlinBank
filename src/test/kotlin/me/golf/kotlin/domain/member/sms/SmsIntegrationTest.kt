package me.golf.kotlin.domain.member.sms

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.kotlin.commonutil.IntegrationTest
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.sms.dto.SendAuthNumberApiRequestDto
import me.golf.kotlin.domain.member.sms.dto.SmsAuthApiRequestDto
import me.golf.kotlin.domain.member.sms.repository.AuthNumberRedisRepository
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.domain.member.util.GivenMember.phoneNumber
import me.golf.kotlin.domain.member.util.GivenMember.toMember
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

class SmsIntegrationTest

@Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val authNumberRedisRepository: AuthNumberRedisRepository,
    private val tokenProvider: TokenProvider,
    private val memberRepository: MemberRepository,
    private val customUserDetailsService: CustomUserDetailsService
): IntegrationTest(){

    var member: Member = toMember()
    var token: String = ""

    @BeforeEach
    fun init() {
        member = memberRepository.save(toMember())

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        authNumberRedisRepository.saveAuthNumber(phoneNumber.substring(4), 1234)

        token = tokenProvider.createToken(member.id, authToken).accessToken
    }

    @Test
    @DisplayName("인증 번호 발급 통합테스트")
    fun publishAuthNumberIntegration() {
        // given
        val requestDto = SendAuthNumberApiRequestDto(phoneNumber)
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = mockMvc.perform(
            post("/api/v2/members/phone/auth-number")
                .header("Authorization", "Bearer $token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        perform.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("redis에 저장된 인증번호와 비교해서 인증한다.")
    @Rollback(false)
    fun authorize() {
        // given
        val requestDto = SmsAuthApiRequestDto(phoneNumber, 1234)
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = mockMvc.perform(
            post("/api/v2/members/phone")
                .header("Authorization", "Bearer $token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        perform.andExpect(status().isOk)
            .andDo(print())

        memberRepository.delete(member)
    }
}