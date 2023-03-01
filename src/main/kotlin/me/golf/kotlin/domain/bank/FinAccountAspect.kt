package me.golf.kotlin.domain.bank

import lombok.extern.slf4j.Slf4j
import me.golf.kotlin.domain.bank.application.BankAccountQueryService
import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.PublishRegisterNumberRequestDto
import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.global.security.CustomUserDetails
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Slf4j
@Aspect
@Component
class FinAccountAspect(
    private val bankAccountQueryService: BankAccountQueryService,
    private val bankAccountApiClient: BankAccountApiClient,
) {

    @Around("@annotation(me.golf.kotlin.domain.bank.RequireFinAccount)")
    fun validateAndGet(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method

        val lookupType = method.getAnnotation(RequireFinAccount::class.java).type

        val args = joinPoint.args

        if (lookupType == LookupType.ONE) {
            if (args.size == 2 && args[0] is CustomUserDetails && args[1] is Long) {
                val memberId = (args[0] as CustomUserDetails).memberId
                val bankId = args[1] as Long

                val bankAccount = bankAccountQueryService.getBankAccount(bankId, memberId)
                var registerNumber = bankAccount.registerNumber

                if (registerNumber == "-1") {
                    val requestDto =
                        PublishRegisterNumberRequestDto.of(true, bankAccount.bankName.code, bankAccount.number)

                    registerNumber = bankAccountApiClient.publishRegisterNumberConnection(requestDto)
                }

                if (bankAccount.finAccount == "-1") {
                    val finAccount = bankAccountApiClient.getFinAccount(registerNumber)
                    bankAccount.updateFinAccountAndRegisterNumber(finAccount, registerNumber)
                }
            }
        }

        if (lookupType == LookupType.SEVERAL) {
            if (args.isNotEmpty() && args[0] is CustomUserDetails) {
                val memberId = (args[0] as CustomUserDetails).memberId

                val bankAccounts = bankAccountQueryService.getBankAccountsBy(memberId)
                    .filter { it.registerNumber == "-1" }

                bankAccounts
                    .filter { it.registerNumber == "-1" }
                    .forEach { it.updateFinAccountAndRegisterNumber(it.finAccount, getRegisterNumber(it)) }

                bankAccounts
                    .filter { it.registerNumber == "-1" }
                    .forEach {
                        it.updateFinAccountAndRegisterNumber(
                            bankAccountApiClient.getFinAccount(it.registerNumber),
                            it.registerNumber
                        )
                    }
            }
        }

        return joinPoint.proceed()
    }

    private fun getRegisterNumber(ac: BankAccount) =
        bankAccountApiClient.publishRegisterNumberConnection(
            PublishRegisterNumberRequestDto.of(true, ac.bankName.code, ac.number)
        )
}