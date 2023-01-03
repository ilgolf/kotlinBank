package me.golf.kotlin.domain.bank.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class TooMuchTransferAmountException : BusinessException(ErrorCode.TOO_MUCH_AMOUNT) {

}
