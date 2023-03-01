package me.golf.kotlin.domain.member.sms.repository

interface AuthNumberRepository {

    fun save(key: String, authNumber: String)

    fun findByIdOrNull(key: String): String?
}