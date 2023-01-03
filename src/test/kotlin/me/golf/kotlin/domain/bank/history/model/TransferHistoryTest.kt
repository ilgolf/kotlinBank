package me.golf.kotlin.domain.bank.history.model

import me.golf.kotlin.domain.bank.history.model.utils.TestTransferHistoryUtils
import me.golf.kotlin.domain.member.util.GivenMember
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
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
            fromMemberId = 1L,
            toMemberId = 2L,
            from = GivenMember.toMember(),
            to = TestTransferHistoryUtils.getToMember()
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