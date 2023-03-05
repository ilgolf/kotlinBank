package me.golf.kotlin.domain.bank

import me.golf.kotlin.domain.bank.application.BankAccountQueryService
import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.PublishRegisterNumberRequestDto
import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_NH_VALUE
import me.golf.kotlin.global.security.CustomUserDetails
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class FinAccountAspect(
    private val bankAccountQueryService: BankAccountQueryService,
    private val bankAccountApiClient: BankAccountApiClient,
    private val bankAccountRepository: BankAccountRepository
) {

    private val log = LoggerFactory.getLogger(FinAccountAspect::class.java)

    @Around("@annotation(me.golf.kotlin.domain.bank.RequireFinAccount)")
    fun validateAndGet(joinPoint: ProceedingJoinPoint): Any? {
        val method = (joinPoint.signature as MethodSignature).method
        val lookupType = method.getAnnotation(RequireFinAccount::class.java).type

        val args = joinPoint.args

        return when (lookupType) {
            LookupType.ONE -> validateAndGetOne(args, joinPoint)
            LookupType.SEVERAL -> validateAndGetSeveral(args, joinPoint)
        }
    }

    private fun validateAndGetOne(args: Array<Any>, joinPoint: ProceedingJoinPoint): Any? {
        if (args.size != 2 || args[0] !is CustomUserDetails || args[1] !is Long) {
            return null
        }

        val userDetails = args[0] as CustomUserDetails
        val bankId = args[1] as Long
        val bankAccount = bankAccountQueryService.getBankAccount(bankId, userDetails.memberId)
        var registerNumber = bankAccount.registerNumber

        if (registerNumber == DEFAULT_NH_VALUE) {
            log.info("등록 번호 발급 진행 ID 정보 : {}", bankAccount.id)

            registerNumber = bankAccountApiClient.publishRegisterNumberConnection(
                PublishRegisterNumberRequestDto.of(true, bankAccount.bankName.code, bankAccount.number))
        }

        val finAccount = getFinAccount(bankAccount, registerNumber)

        bankAccountRepository.updateFinAccountAndRegisterNumber(finAccount = finAccount, registerNumber = registerNumber, bankId)

        return joinPoint.proceed()
    }

    private fun validateAndGetSeveral(args: Array<Any>, joinPoint: ProceedingJoinPoint): Any? {
        if (args.isEmpty() || args[0] !is CustomUserDetails) {
            return null
        }

        val userDetails = args[0] as CustomUserDetails
        val memberId = userDetails.memberId

        val bankAccounts = bankAccountQueryService.getBankAccountsBy(memberId)
            .filter { it.registerNumber == DEFAULT_NH_VALUE }

        bankAccounts.forEach {
            val registerNumber = getRegisterNumber(it)
            val finAccount = bankAccountApiClient.getFinAccount(registerNumber)
            bankAccountRepository.updateFinAccountAndRegisterNumber(finAccount, registerNumber, it.id)
        }

        return joinPoint.proceed()
    }

    private fun getFinAccount(bankAccount: BankAccount, registerNumber: String) =
        if (bankAccount.finAccount == DEFAULT_NH_VALUE) bankAccountApiClient.getFinAccount(registerNumber)
        else bankAccount.finAccount

    private fun getRegisterNumber(bankAccount: BankAccount) =
        bankAccountApiClient.publishRegisterNumberConnection(
            PublishRegisterNumberRequestDto.of(true, bankAccount.bankName.code, bankAccount.number)
        )
}