package me.golf.kotlin.domain.member.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class PhoneNumberMissMatchException(password: String) : BusinessException(
    ErrorCode.PASSWORD_CONFIRM_FAIL, password
)
