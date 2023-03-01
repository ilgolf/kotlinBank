package me.golf.kotlin.domain.bank.application

import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.BankAccountSaveRequestDto
import me.golf.kotlin.domain.bank.dto.PublishRegisterNumberRequestDto
import me.golf.kotlin.domain.bank.dto.SimpleBankAccountIdResponseDto
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BankAccountCommandService(
    private val bankAccountRepository: BankAccountRepository,
    private val encoder: PasswordEncoder,
    private val bankAccountApiClient: BankAccountApiClient,
    private val bankAccountLockService: BankAccountLockService
) {

    fun save(requestDto: BankAccountSaveRequestDto): SimpleBankAccountIdResponseDto {

        try {
            if (!bankAccountLockService.tryLock(requestDto.name)) {
                throw BankAccountException.AlreadyLockException()
            }

            this.validateDuplicationByAccountNumber(requestDto.number)
            this.validationDuplicationByName(requestDto.name)

            // 우선 무조건 입출금 가능 이 후 변경 ?
            val registerNumber = getRegisterNumber(requestDto)
            val finAccount = bankAccountApiClient.getFinAccount(registerNumber)

            val bankAccount = requestDto
                .toEntity(registerNumber, finAccount)
                .encodePassword(encoder)

            return SimpleBankAccountIdResponseDto(bankAccountRepository.save(bankAccount).id)
        } finally {
            bankAccountLockService.unlock(requestDto.name)
        }
    }

    fun update(nickname: String, bankAccountId: Long, memberId: Long) {
        val bankAccount = bankAccountRepository.findByIdAndMemberId(bankAccountId, memberId)
            ?: throw BankAccountException.NotFoundException()

        bankAccount.updateAccountName(nickname)
    }

    fun delete(bankAccountId: Long, memberId: Long) {
        val bankAccount = bankAccountRepository.findByIdAndMemberId(bankAccountId, memberId)
            ?: throw BankAccountException.NotFoundException()

        bankAccount.delete()
    }

    fun getRegisterNumber(requestDto: BankAccountSaveRequestDto): String {
        val finAccountRequestDto = PublishRegisterNumberRequestDto
            .of(true, requestDto.bankName.code, requestDto.number)

        return bankAccountApiClient.publishRegisterNumberConnection(finAccountRequestDto)
    }


    private fun validationDuplicationByName(name: String) {
        check(!bankAccountRepository.existsByName(name)) { throw BankAccountException.NameDuplicationException(name) }
    }

    private fun validateDuplicationByAccountNumber(number: String) {
        check(!bankAccountRepository.existsByNumber(number)) { throw BankAccountException.NumberDuplicationException(number) }
    }
}
