package me.golf.kotlin.domain.bank.history.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.bank.history.dto.HistorySummaryResponseDto
import me.golf.kotlin.domain.bank.history.model.TransferHistoryRepository
import me.golf.kotlin.domain.bank.history.model.TransferStatus
import me.golf.kotlin.domain.bank.history.model.utils.TestTransferHistoryUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.SliceImpl
import java.time.LocalDateTime

internal class PaymentHistoryServiceTest {

    private val transferHistoryRepository: TransferHistoryRepository = mockk()
    private lateinit var paymentHistoryService: PaymentHistoryService

    @BeforeEach
    fun init() {
        paymentHistoryService = PaymentHistoryService(transferHistoryRepository)
    }

    @Test
    @DisplayName("송금 이력 list를 보여준다.")
    fun getHistories() {
        // given
        val transferHistories = arrayListOf(
            HistorySummaryResponseDto(
                transferMoney= TestTransferHistoryUtils.transferMoney,
                client= TestTransferHistoryUtils.client,
                transferStatus= TransferStatus.WITHDRAWAL,
                memberNickname= "테스트 회원",
                createdAt= LocalDateTime.now()
            )
        )

        val responseDtos = SliceImpl(transferHistories, PageRequest.of(0, 10), true)

        every { transferHistoryRepository.findHistoryByBankAccountId(any(), any(), any()) } returns responseDtos

        // when
        val histories = paymentHistoryService.getHistories(1L, 1L, PageRequest.of(0, 10))

        // then
        assertThat(histories.contents[0].memberNickname).isEqualTo("테스트 회원")
    }
}