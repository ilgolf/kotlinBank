package me.golf.kotlin.global.common

object RedisPolicy {
    const val AUTH_KEY = "Authorization"
    const val AUTH_TTL = 180L
    const val PHONE_AUTH_KEY = "PHONE_NUMBER" // PHONE_NUMBER::12341234 = 3432
    const val PHONE_AUTH_TTL = 5L
}