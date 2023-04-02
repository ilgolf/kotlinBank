package me.golf.kotlin.domain.bank.payment.application

import io.mockk.mockk
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.amqp.rabbit.core.RabbitTemplate

internal class PaymentQueueServiceTest {

    private val bankAccountRepository: BankAccountRepository = mockk()
    private val rabbitTemplate: RabbitTemplate = mockk()
    lateinit var paymentQueueService: PaymentQueueService
    private val mockQueueName: String = "mockQueue"

    @BeforeEach
    fun setup() {
        paymentQueueService = PaymentQueueService(bankAccountRepository, rabbitTemplate, mockQueueName, mockQueueName, "topic")
    }

    @Test
    @DisplayName("finAccount 정보를 받아와 queue에 요청을 담아 처리한다.")
    fun test1() {
        // given

        // when

        // then

    }

    @Test
    fun test2() {
        // given


        // when

        // then

    }
}