package me.golf.kotlin.global.exception.error

open class BusinessException(
    val errorCode: ErrorCode,
    val value: String?
) : RuntimeException(errorCode.message + ": $value") {

    constructor(errorCode: ErrorCode): this(errorCode, "")
}