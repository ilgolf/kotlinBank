package me.golf.kotlin.domain.account.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.account.error.InvalidRefreshTokenException
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.jwt.dto.TokenBaseDto
import me.golf.kotlin.global.security.CustomUserDetails
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder

internal class AccountServiceTest {

    private val customUserDetailsService = mockk<CustomUserDetailsService>()
    private val tokenProvider = mockk<TokenProvider>()
    private val managerBuilder = mockk<AuthenticationManagerBuilder>()
    private val accountService = AccountService(customUserDetailsService, tokenProvider, managerBuilder)

    @Test
    @DisplayName("email과 password로 인증을 한다.")
    fun login() {
        // given
        val customUserDetails = CustomUserDetails.of(GivenMember.toMember())
        val tokenDto = TokenBaseDto("accessToken", "refreshToken")
        val authToken = UsernamePasswordAuthenticationToken(customUserDetails, "")

        every { customUserDetailsService.loadUserByEmail(any()) } returns customUserDetails
        every { tokenProvider.createToken(any(), any()) } returns tokenDto
        every { managerBuilder.`object`.authenticate(any()) } returns authToken

        // when
        val result = accountService.login(GivenMember.email.value, GivenMember.password)

        // then
        assertAll(
            { assertThat(result.accessToken).isEqualTo("accessToken") },
            { assertThat(result.refreshToken).isEqualTo("refreshToken") }
        )
    }

    @Test
    @DisplayName("refreshToken을 받아 accessToken을 새로 발급한다.")
    fun reissue() {
        // given
        val refreshToken = "refreshToken"
        val customUserDetails = CustomUserDetails.of(GivenMember.toMember())
        val authentication = UsernamePasswordAuthenticationToken(customUserDetails, "")

        every { tokenProvider.validateToken(any()) } returns true
        every { tokenProvider.getAuthentication(any()) } returns authentication
        every { tokenProvider.createToken(any(), any()) } returns TokenBaseDto("accessToken", refreshToken)

        // when
        val result = accountService.reissue(refreshToken)

        // then
        assertThat(result).isEqualTo("accessToken")
    }

    @Test
    @DisplayName("refresh 토큰이 올바르지 않으면 예외가 발생한다.")
    fun reissueFailByInvalidToken() {
        // given
        val refreshToken = "refreshToken"
        val customUserDetails = CustomUserDetails.of(GivenMember.toMember())
        val authentication = UsernamePasswordAuthenticationToken(customUserDetails, "")

        every { tokenProvider.validateToken(any()) } returns false
        every { tokenProvider.getAuthentication(any()) } returns authentication

        // when
        val exception = catchException { accountService.reissue(refreshToken) }

        // then
        assertThat(exception).isInstanceOf(InvalidRefreshTokenException::class.java)
    }
}