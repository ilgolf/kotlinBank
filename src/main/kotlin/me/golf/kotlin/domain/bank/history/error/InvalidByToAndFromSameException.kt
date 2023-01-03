package me.golf.kotlin.domain.bank.history.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class InvalidByToAndFromSameException : BusinessException(ErrorCode.INVALID_TO_AND_FROM_SAME)
