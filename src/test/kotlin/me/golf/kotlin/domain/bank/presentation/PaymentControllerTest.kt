package me.golf.kotlin.domain.bank.presentation

import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.golf.kotlin.domain.bank.payment.application.PaymentQueueService
import me.golf.kotlin.domain.bank.payment.dto.PaymentApiRequestDto
import me.golf.kotlin.domain.bank.payment.dto.RefundApiRequestDto
import me.golf.kotlin.domain.bank.payment.presentation.PaymentController
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.security.CustomUserDetails
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class PaymentControllerTest {

    private val paymentQueueService: PaymentQueueService = mockk()
    lateinit var paymentController: PaymentController
    private val customUserDetails = CustomUserDetails.of(GivenMember.toMember())

    @BeforeEach
    fun setup() {
        paymentController = PaymentController(paymentQueueService)
    }

    @Test
    @DisplayName("결제 요청을 보내 성공하면 200 OK")
    fun pay() {
        // given
        val requestDto = PaymentApiRequestDto(
            bankId = 1L,
            fromName = "테스트용 계좌2",
            transferMoney = "10000"
        )

        coJustRun { paymentQueueService.pay(any()) }

        // when
        val result = runBlocking { paymentController.pay(requestDto, customUserDetails) }

        // then
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
    
    @Test
    @DisplayName("환불 요청을 보내 성공하면 200 OK")
    fun refund() {
        // given
        val requestDto = RefundApiRequestDto(
            bankId = 1L,
            accountNumber  = "31239129083",
            bankName = "농협은행",
            transferMoney = "10000",
            refundCause = "오결제",
            fromName = "소비자"
        )

        coJustRun { paymentQueueService.refund(any()) }

        // when
        val result = runBlocking { paymentController.refund(requestDto, customUserDetails) }

        // that
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}