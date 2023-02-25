package me.golf.kotlin

import me.golf.kotlin.commonutil.IntegrationTest
import me.golf.kotlin.domain.bank.TestBankAccountUtils
import me.golf.kotlin.domain.bank.application.BankAccountCommandService
import me.golf.kotlin.domain.bank.dto.BankAccountSaveRequestDto
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BankAccountConcurrencyTest

@Autowired
constructor(
    private val bankAccountCommandService: BankAccountCommandService,
    private val bankAccountRepository: BankAccountRepository,
    private val memberRepository: MemberRepository
): IntegrationTest() {

    private val log = LoggerFactory.getLogger(BankAccountConcurrencyTest::class.java)
    lateinit var executorService: ExecutorService
    lateinit var member: Member

    @BeforeEach
    fun init() {
        executorService = Executors.newFixedThreadPool(5)
        member = memberRepository.save(GivenMember.toMember())
    }

    @Test
    @DisplayName("동시성 이슈 확인용 테스트")
    fun save() {
        val requestDto = BankAccountSaveRequestDto.testInitializer(TestBankAccountUtils.mockBankAccount(), TestBankAccountUtils.memberId)

        val latch = CountDownLatch(2)

        // when
        for (i in 1 .. 2) {
            executorService.execute {
                try {
                    bankAccountCommandService.save(requestDto)
                    latch.countDown()
                } catch (e: Exception) {
                    log.error("error : {}", e.toString())
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        val count =
            bankAccountRepository.countByNumberAndName(TestBankAccountUtils.number, TestBankAccountUtils.name)

        assertThat(count).isEqualTo(1)
    }
}