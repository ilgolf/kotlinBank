package me.golf.kotlin.domain.member.util

import me.golf.kotlin.domain.member.sms.model.AuthNumber

object GivenAuthNumber {
    const val id = "5062-6098"
    const val authNumber = 1234

    fun toEntity(): AuthNumber {
        return AuthNumber(id, authNumber)
    }
}