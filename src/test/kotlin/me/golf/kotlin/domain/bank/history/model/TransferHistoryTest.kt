package me.golf.kotlin.domain.bank.history.model

import me.golf.kotlin.domain.bank.history.model.utils.TestTransferHistoryUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class TransferHistoryTest {

    @Test
    @DisplayName("id가 같으면 같은 객체")
    fun equalTest() {
        // given
        val givenTransferHistory = TransferHistory(
            transferMoney = BigDecimal.valueOf(6000),
            client = 1L,
            transferStatus = TransferStatus.DEPOSIT,
            bankId = 1L
        )

        val transferHistory = TestTransferHistoryUtils.toEntity()

        transferHistory.id = 1L
        givenTransferHistory.id = 1L

        // when
        val result = transferHistory == givenTransferHistory

        // then
        assertThat(result).isTrue
    }
}