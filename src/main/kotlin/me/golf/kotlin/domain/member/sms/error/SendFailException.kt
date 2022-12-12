package me.golf.kotlin.domain.member.sms.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class SendFailException(phoneNumber: String) : BusinessException(ErrorCode.SEND_FAIL_ERROR, phoneNumber) {

}
