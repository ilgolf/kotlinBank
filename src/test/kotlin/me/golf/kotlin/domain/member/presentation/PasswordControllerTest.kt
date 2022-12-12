package me.golf.kotlin.domain.member.presentation

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.member.application.PasswordService
import me.golf.kotlin.domain.member.dto.PasswordApiFindPasswordRequestDto
import me.golf.kotlin.domain.member.dto.PasswordApiUpdateRequestDto
import me.golf.kotlin.domain.member.dto.TempPasswordApiResponseDto
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.security.CustomUserDetails
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.http.HttpStatus

internal class PasswordControllerTest {

    private val passwordService: PasswordService = mockk(relaxed = true)
    private val passwordController = PasswordController(passwordService)

    @Test
    @DisplayName("비밀번호를 변경하고 성공 시 200 ok로 응답한다.")
    fun updatePassword() {
        // given
        val requestDto = PasswordApiUpdateRequestDto("qwer1234", "qwer1234")
        val customUserDetails = CustomUserDetails.of(GivenMember.toMember())

        // when
        val response = passwordController.updatePassword(requestDto, customUserDetails)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("비밀번호를 재발급 받고 성공 시 200 ok로 응답한다.")
    fun publishTempPassword() {
        // given
        val requestDto = PasswordApiFindPasswordRequestDto(true, GivenMember.phoneNumber)
        val responseDto = TempPasswordApiResponseDto(tempPassword = "jewqk31231")
        val customUserDetails = CustomUserDetails.of(GivenMember.toMember())

        every { passwordService.publishTempPassword(any()) } returns responseDto

        // when
        val response = passwordController.publishTempPassword(requestDto, customUserDetails)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("")
    fun publishFailByPhoneConfirmDeny() {
        // given
        val requestDto = PasswordApiFindPasswordRequestDto(false, GivenMember.phoneNumber)
        val customUserDetails = CustomUserDetails.of(GivenMember.toMember())

        // when
        val exception = catchException { passwordController.publishTempPassword(requestDto, customUserDetails) }

        // then
        assertThat(exception).hasMessage("핸드폰 인증을 하지 않으셨습니다.: ${requestDto.phoneNumber}")
    }
}