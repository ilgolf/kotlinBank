package me.golf.kotlin.domain.account

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.kotlin.commonutil.IntegrationTest
import me.golf.kotlin.domain.account.dto.LoginRequestDto
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
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.servlet.http.Cookie

class AccountIntegrationTest
@Autowired constructor(
    private val memberRepository: MemberRepository,
    private val customUserDetailsService: CustomUserDetailsService,
    private val objectMapper: ObjectMapper,
    private val tokenProvider: TokenProvider
): IntegrationTest() {

    var member: Member = GivenMember.toMember()
    var token: String = ""
    var refreshToken: String = ""

    @BeforeEach
    fun init() {
        member = memberRepository.save(GivenMember.toMember())

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())
        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        val tokenDto = tokenProvider.createToken(member.id, authToken)

        token = tokenDto.accessToken
        refreshToken = tokenDto.refreshToken
    }

    @Test
    @DisplayName("로그인 통합 테스트")
    fun login() {
        // given
        val requestDto = LoginRequestDto(GivenMember.email.value, GivenMember.password)
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )

        // then
        perform.andExpect(status().isOk).andDo(print())
    }

    @Test
    @DisplayName("로그인 시 빈 값이 들어오면 400 bad Request")
    fun loginFailByInvalidValue() {
        // given
        val requestDto = LoginRequestDto("", " ")
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )

        // then
        perform.andExpect(status().isBadRequest).andDo(print())
    }

    @Test
    @DisplayName("로그인 시 없는 이메일이면 400 bad Request")
    fun loginFailByNotFound() {
        // given
        val requestDto = LoginRequestDto("fasdf@naver.com", "1231312")
        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )

        // then
        perform.andExpect(status().isBadRequest).andDo(print())
    }

    @Test
    @DisplayName("로그아웃 통합 테스트")
    fun logout() {
        // given

        // when
        val perform = mockMvc.perform(
            delete("/api/v2/account")
                .header("Authorization", "Bearer $token")
                .cookie(Cookie("refreshToken", refreshToken))
        )

        // then
        perform.andExpect(status().isNoContent).andDo(print())
    }

    @Test
    @DisplayName("토큰 재발행 통합 테스트")
    fun reissue() {
        // given

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/account/reissue")
                .cookie(Cookie("refreshToken", refreshToken))
        )

        // then
        perform.andExpect(status().isOk).andDo(print())
    }

    @Test
    @DisplayName("토큰 재발행 시 잘못된 토큰 으로 인한 오류 발생 통합 테스트")
    fun reissueByInvalidToken() {
        // given

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/account/reissue")
                .cookie(Cookie("refreshToken", "refreshToken"))
        )

        // then
        perform.andExpect(status().isBadRequest).andDo(print())
    }

    @Test
    @DisplayName("토큰 재발행 시 없는 토큰 으로 인한 오류 발생 통합 테스트")
    fun reissueByNullToken() {
        // given

        // when
        val perform = mockMvc.perform(
            post("/api/v2/public/account/reissue"))

        // then
        perform.andExpect(status().isInternalServerError).andDo(print())
    }
}