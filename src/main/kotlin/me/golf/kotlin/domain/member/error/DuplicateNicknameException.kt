package me.golf.kotlin.domain.member.error

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class DuplicateNicknameException(nickname: String) : BusinessException(ErrorCode.DUPLICATE_NICKNAME, nickname)
