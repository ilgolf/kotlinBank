package me.golf.kotlin.domain.member.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class DuplicateEmailException(email: String) : BusinessException(ErrorCode.DUPLICATE_EMAIL, email)