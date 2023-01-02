package me.golf.kotlin.domain.member.sms

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.kotlin.commonutil.IntegrationTest
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.sms.dto.SendAuthNumberApiRequestDto
import me.golf.kotlin.domain.member.sms.dto.SmsAuthApiRequestDto
import me.golf.kotlin.domain.member.sms.model.AuthNumber
import me.golf.kotlin.domain.member.sms.repository.AuthNumberRepository
import me.golf.kotlin.domain.member.util.GivenMember.phoneNumber
import me.golf.kotlin.domain.member.util.GivenMember.toMember
import me.golf.kotlin.global.config.RedisConfig
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.annotation.Rollback
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Import(RedisConfig::class)
class SmsIntegrationTest

@Autowired constructor(
    private val objectMapper: ObjectMapper,
): IntegrationTest(){

    @Test
    @DisplayName("인증 번호 발급 통합테스트")
    fun publishAuthNumberIntegration() {
        // given
        val requestDto = SendAuthNumberApiRequestDto(phoneNumber)
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/members/phone/auth-number")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        perform.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("redis에 저장된 인증번호와 비교해서 인증한다.")
    fun authorize() {
        // given
        val requestDto = SmsAuthApiRequestDto(phoneNumber, 1234)
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/members/phone")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        perform.andExpect(status().isOk)
            .andDo(print())
    }
}