package me.golf.kotlin.domain.member.sms.repository

interface AuthNumberRepository {

    fun saveAuthNumber(key: String, secureNumber: Int) // ex. 1234-1234

    fun getAuthNumber(key: String): Int?
}