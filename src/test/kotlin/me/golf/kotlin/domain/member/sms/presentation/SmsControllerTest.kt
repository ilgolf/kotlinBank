package me.golf.kotlin.domain.member.sms.presentation

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.member.sms.application.SmsService
import me.golf.kotlin.domain.member.sms.dto.SendAuthNumberApiRequestDto
import me.golf.kotlin.domain.member.sms.dto.SmsAuthApiRequestDto
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.common.SingleCustomMessageSentResponse
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.http.HttpStatus

internal class SmsControllerTest {

    private val smsService: SmsService = mockk()
    private val smsController = SmsController(smsService)

    @Test
    @DisplayName("sms 인증번호 발송하고 성공 시 200 ok 응답한다.")
    fun sendAuthNumber() {
        // given
        val requestDto = SendAuthNumberApiRequestDto(GivenMember.phoneNumber)
        val responseDto = SingleCustomMessageSentResponse(
            to = "010-1234-1234",
            from = "010-5062-6098",
            country = "korea"
        )

        every { smsService.sendAuthNumber(requestDto.phoneNumber) } returns responseDto

        // when
        val response = smsController.sendAuthNumber(requestDto)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("sms 인증번호로 인증을 한다.")
    fun authorizePhoneNumber() {
        // given
        val requestDto = SmsAuthApiRequestDto(GivenMember.phoneNumber, 1234)

        every { smsService.authorizePhoneNumber(any(), any()) } returns true

        // when
        val response = smsController.authorizePhoneNumber(requestDto)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}