package me.golf.kotlin.domain.account.presentation

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.account.application.AccountService
import me.golf.kotlin.domain.account.dto.LoginRequestDto
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.jwt.dto.TokenBaseDto
import me.golf.kotlin.global.security.CustomUserDetails
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.MethodArgumentNotValidException

internal class AccountControllerTest {

    private val accountService = mockk<AccountService>()
    private val accountController = AccountController(accountService)

    @Test
    @DisplayName("로그인을 하면 안증 토큰을 부여 받는다.")
    fun login() {
        // given
        val requestDto = LoginRequestDto(GivenMember.email.value, GivenMember.password)

        every { accountService.login(any(), any()) } returns TokenBaseDto("accessToken", "refreshToken")

        // when
        val response = accountController.login(requestDto)

        // then
        assertAll(
            { assertThat(response.headers["Set-Cookie"]?.get(0)).contains("refreshToken") },
            { assertThat(response.body?.accessToken).isEqualTo("accessToken") },
            { assertThat(response.statusCode).isEqualTo(HttpStatus.OK) }
        )
    }

    @Test
    @DisplayName("로그아웃하고 refreshToken을 삭제한다.")
    fun logout() {
        // given
        val response = MockHttpServletResponse()

        // when
        val result = accountController.logout(response)

        // then
        assertAll(
            { assertThat(result.headers["Set-Cookie"]).isNull() },
            { assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT) }
        )
    }

    @Test
    @DisplayName("토큰이 만료되면 재발급 후 200 ok로 응답한다.")
    fun reissueToken() {
        // given
        val refreshToken = "refreshToken"

        every { accountService.reissue(any()) } returns "accessToken"

        // when
        val result = accountController.reissueToken(refreshToken)

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}