package me.golf.kotlin.domain.bank.application

import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.nh.dto.PublishRegisterNumberRequestDto
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_NH_VALUE
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FinAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val bankAccountApiClient: BankAccountApiClient
) {

    private val log = LoggerFactory.getLogger(FinAccountService::class.java)

    @Transactional
    fun validateAndGetFinAccount(memberId: Long, bankId: Long): Boolean {
        val bankAccount = bankAccountRepository.findByIdAndMemberId(bankId, memberId)
            ?: throw BankAccountException.NotFoundException()

        var registerNumber = bankAccount.registerNumber
        var finAccount = bankAccount.finAccount

        if (registerNumber == DEFAULT_NH_VALUE) {
            log.info("등록 번호 존재 하지 않음 발급 시작 : {} 회원 ID : {}", bankAccount.id, memberId)
            registerNumber = bankAccountApiClient.publishRegisterNumberConnection(
                PublishRegisterNumberRequestDto.of(true, bankAccount.bankName.code, bankAccount.number))
        }

        if (finAccount == DEFAULT_NH_VALUE) {
            log.info("핀-어카운트 존재 하지 않음 발급 시작 : {} 회원 ID : {}", bankAccount.id, memberId)
            finAccount = bankAccountApiClient.getFinAccount(registerNumber)
        }

        bankAccount.updateFinAccountAndRegisterNumber(finAccount, registerNumber)

        return finAccount != DEFAULT_NH_VALUE && registerNumber != DEFAULT_NH_VALUE
    }
}