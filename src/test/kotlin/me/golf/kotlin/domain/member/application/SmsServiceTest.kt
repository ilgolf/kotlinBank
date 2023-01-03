package me.golf.kotlin.domain.member.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.member.sms.application.MessageService
import me.golf.kotlin.domain.member.sms.application.SmsService
import me.golf.kotlin.domain.member.sms.error.SecureNumberNotFoundException
import me.golf.kotlin.domain.member.sms.error.SendFailException
import me.golf.kotlin.domain.member.sms.repository.AuthNumberRepository
import me.golf.kotlin.domain.member.util.TestAuthNumberUtils
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.common.SingleCustomMessageSentResponse
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName

internal class SmsServiceTest {

    private val messageService: MessageService = mockk()
    private val authNumberRepository: AuthNumberRepository = mockk(relaxed = true)
    private val smsService: SmsService = SmsService("01012341234", messageService, authNumberRepository)

    @Test
    @DisplayName("인증 번호 5자리를 만들 수 있다.")
    fun sendAuthNumber() {
        // given
        val phoneNumber = GivenMember.phoneNumber

        every { messageService.sendOne(any()) } returns SingleCustomMessageSentResponse(
            to = "010-1234-1234",
            from = phoneNumber,
            country = "korea",
        )

        // when
        val response = smsService.sendAuthNumber(phoneNumber)

        // then
        assertThat(response.from).isEqualTo(phoneNumber)
    }

    @Test
    @DisplayName("인증번호를 받아와 저장한 인증번호와 비교한다.")
    fun authorizePhoneNumber() {
        // given
        val phoneNumber = "010-5062-6098"
        val authNumber = 1234

        every { authNumberRepository.findByIdOrNull(any()) } returns TestAuthNumberUtils.authNumber.toString()

        // when
        val result = smsService.authorizePhoneNumber(phoneNumber, authNumber)

        // then
        assertThat(result).isTrue
    }

    @Test
    @DisplayName("인증번호를 받아와 저장한 인증번호와 비교에 실패하면 예외처리 한다.")
    fun authorizePhoneNumberNotFoundNumber() {
        // given
        val phoneNumber = "010-5062-6098"
        val authNumber = 1234

        every { authNumberRepository.findByIdOrNull(any()) } returns null

        // when
        val exception = catchException { smsService.authorizePhoneNumber(phoneNumber, authNumber) }

        // then
        assertThat(exception).isInstanceOf(SecureNumberNotFoundException::class.java)
    }


    @Test
    @DisplayName("인증 번호를 전송하지 못하면 예외처리 한다.")
    fun sendAuthNumberSendFail() {
        // given
        val phoneNumber = GivenMember.phoneNumber

        every { messageService.sendOne(any()) } throws SendFailException(phoneNumber)

        // when
        val exception = catchException { smsService.sendAuthNumber(phoneNumber) }

        // then
        assertThat(exception).isInstanceOf(SendFailException::class.java)
    }
}