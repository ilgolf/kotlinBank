package me.golf.kotlin.domain.bank.application

import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.BankAccountSaveRequestDto
import me.golf.kotlin.domain.bank.dto.PublishFinAccountRequestDto
import me.golf.kotlin.domain.bank.dto.SimpleBankAccountIdResponseDto
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.member.application.MemberQueryService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BankAccountCommandService(
    private val bankAccountRepository: BankAccountRepository,
    private val encoder: PasswordEncoder,
    private val bankAccountApiClient: BankAccountApiClient,
    private val memberQueryService: MemberQueryService,
    private val bankAccountLockService: BankAccountLockService
) {

    fun save(requestDto: BankAccountSaveRequestDto): SimpleBankAccountIdResponseDto {

        try {
            if (!bankAccountLockService.tryLock(requestDto.name)) {
                throw BankAccountException.AlreadyLockException()
            }

            this.validateDuplicationByAccountNumber(requestDto.number)
            this.validationDuplicationByName(requestDto.name)

            val member = memberQueryService.getDetail(requestDto.memberId)

            // 우선 무조건 입출금 가능 이 후 변경 ?
            val finAccountRequestDto = PublishFinAccountRequestDto
                .of(true, member.birth.getStringValue(), requestDto.bankName.code, requestDto.number)

            val bankAccount = requestDto
                .toEntity(getFinAccount(finAccountRequestDto))
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

    private fun getFinAccount(finAccountRequestDto: PublishFinAccountRequestDto) =
        bankAccountApiClient.getFinAccountConnection(finAccountRequestDto)

    private fun validationDuplicationByName(name: String) {
        check(!bankAccountRepository.existsByName(name)) { throw BankAccountException.NameDuplicationException(name) }
    }

    private fun validateDuplicationByAccountNumber(number: String) {
        check(!bankAccountRepository.existsByNumber(number)) { throw BankAccountException.NumberDuplicationException(number) }
    }
}
