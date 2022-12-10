package me.golf.kotlin.domain.member

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.kotlin.commonutil.IntegrationTest
import me.golf.kotlin.domain.member.dto.PasswordApiFindPasswordRequestDto
import me.golf.kotlin.domain.member.dto.PasswordApiUpdateRequestDto
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PasswordIntegrationTest

@Autowired constructor(
    private val tokenProvider: TokenProvider,
    private val objectMapper: ObjectMapper,
    private val customUserDetailsService: CustomUserDetailsService,
    private val memberRepository: MemberRepository
) : IntegrationTest() {

    var member: Member = GivenMember.toMember()
    var token: String = ""

    @BeforeEach
    fun init() {
        member = memberRepository.save(GivenMember.toMember())

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        token = tokenProvider.createToken(member.id, authToken).accessToken
    }

    @Test
    @DisplayName("비밀번호 수정 통합 테스트")
    fun updatePassword() {
        // given
        val requestDto = PasswordApiUpdateRequestDto("query1234@", "query1234@")
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = getUpdatePerform(body)

        // then
        perform
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("비밀번호와 확인용 비밀번호가 다르면 400 badRequest로 응답한다.")
    fun updatePasswordFail() {
        // given
        val requestDto = PasswordApiUpdateRequestDto("query1234@", "query")
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = getUpdatePerform(body)

        // then
        perform
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    @DisplayName("임시 비밀번호 발급 통합 테스트")
    fun publishTempPassword() {
        // given
        val requestDto = PasswordApiFindPasswordRequestDto(true, member.phoneNumber)
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = getPublishPerform(body)

        // then
        perform
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("사전에 sms 인증을 하지 않으면 400 BadRequest")
    fun publishFail() {
        // given
        val requestDto = PasswordApiFindPasswordRequestDto(false, member.phoneNumber)
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = getPublishPerform(body)

        // then
        perform
            .andExpect(status().isBadRequest)
            .andDo(print())
    }

    private fun getUpdatePerform(body: String) = mockMvc.perform(
        put("/api/v2/members/password")
            .header("Authorization", "Bearer $token")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)

    )

    private fun getPublishPerform(body: String): ResultActions {

        return mockMvc.perform(
            post("/api/v2/members/password")
                .header("Authorization", "Bearer $token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )
    }
}