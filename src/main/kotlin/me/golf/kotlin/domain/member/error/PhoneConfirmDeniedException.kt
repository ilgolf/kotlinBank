package me.golf.kotlin.domain.member.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class PhoneConfirmDeniedException(phoneNumber: String): BusinessException(ErrorCode.PHONE_ACCESS_DENIED, phoneNumber) {
}
