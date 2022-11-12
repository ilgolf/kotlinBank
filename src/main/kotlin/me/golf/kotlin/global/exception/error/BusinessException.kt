package me.golf.kotlin.global.exception.error

class BusinessException(val errorCode: ErrorCode): RuntimeException(errorCode.message)