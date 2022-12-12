package me.golf.kotlin.domain.member.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class MemberNotFoundException(memberId: Long) : BusinessException(ErrorCode.MEMBER_NOT_FOUND, memberId.toString())
