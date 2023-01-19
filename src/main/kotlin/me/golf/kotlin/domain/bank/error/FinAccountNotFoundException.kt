package me.golf.kotlin.domain.bank.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class FinAccountNotFoundException: BusinessException(ErrorCode.FIN_ACCOUNT_NOT_FOUND)
