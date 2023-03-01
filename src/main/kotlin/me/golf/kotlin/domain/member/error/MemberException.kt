package me.golf.kotlin.domain.member.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

sealed class MemberException {

    class DuplicateEmailException(email: String) : BusinessException(ErrorCode.DUPLICATE_EMAIL, email)
    class DuplicateNicknameException(nickname: String) : BusinessException(ErrorCode.DUPLICATE_NICKNAME, nickname)
    class MemberNotFoundException(memberId: Long) : BusinessException(ErrorCode.MEMBER_NOT_FOUND, memberId.toString()) {
        constructor(): this(-1)
    }
    class PhoneConfirmDeniedException(phoneNumber: String): BusinessException(ErrorCode.PHONE_ACCESS_DENIED, phoneNumber)
    class PhoneNumberMissMatchException(password: String) : BusinessException(ErrorCode.PASSWORD_CONFIRM_FAIL, password)
}
