package me.golf.kotlin.domain.account.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class InvalidRefreshTokenException : BusinessException(ErrorCode.INVALID_REFRESH_TOKEN)
