package me.golf.kotlin.domain.member.sms.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class SecureNumberNotFoundException(value: String): BusinessException(ErrorCode.SECURE_NUMBER_NOT_FOUND, value)
