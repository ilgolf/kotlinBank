package me.golf.kotlin.domain.bank.history.model

import me.golf.kotlin.commonutil.JpaTest
import me.golf.kotlin.domain.bank.TestBankAccountUtils
import me.golf.kotlin.domain.bank.history.model.utils.TestTransferHistoryUtils
import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

internal class TransferHistoryRepositoryTest

@Autowired
constructor(
    private val memberRepository: MemberRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val transferHistoryRepository: TransferHistoryRepository
) : JpaTest() {

    private lateinit var member: Member
    private lateinit var bankAccount: BankAccount

    @BeforeEach
    fun init() {
        member = memberRepository.save(GivenMember.toMember())
        bankAccount = bankAccountRepository.save(TestBankAccountUtils.createBankAccountBy(member.id))
        transferHistoryRepository.save(TestTransferHistoryUtils.toEntity(bankAccount.id, member.id))
    }

    @Test
    @DisplayName("계좌 조회 이력을 들고 온다.")
    fun findHistoryByBankAccountId() {
        // given

        // when
        val responseDtos =
            transferHistoryRepository.findHistoryByBankAccountId(bankAccount.id, member.id, PageRequest.of(0, 10))

        // then
        assertThat(responseDtos.content[0].transferMoney).isEqualByComparingTo(TestTransferHistoryUtils.transferMoney)
    }
}