package me.golf.kotlin.domain.bank.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

sealed class BankAccountException {

    class AlreadyLockException : BusinessException(ErrorCode.TRY_LOCK_DENIED)
    class NumberDuplicationException(number: String) : BusinessException(ErrorCode.DUPLICATE_ACCOUNT_NUMBER, number)
    class NameDuplicationException(name: String) : BusinessException(ErrorCode.DUPLICATE_ACCOUNT_NICKNAME, name)
    class NotFoundException : BusinessException(ErrorCode.BANK_ACCOUNT_NOT_FOUND)
    class ConvertBankNameDeniedException : BusinessException(ErrorCode.CONVERT_BANK_NAME_DENIED)
    class FinAccountNotFoundException: BusinessException(ErrorCode.FIN_ACCOUNT_NOT_FOUND)
}