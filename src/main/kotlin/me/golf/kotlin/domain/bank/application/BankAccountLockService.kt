package me.golf.kotlin.domain.bank.application

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class BankAccountLockService(
    private val redisTemplate: RedisTemplate<String, Int>
) {

    companion object {
        const val BANK_ACCOUNT_LOCK_KEY = "BANK_LOCK::"
    }

    fun tryLock(name: String): Boolean {

        val key = BANK_ACCOUNT_LOCK_KEY + name
        redisTemplate.expire(key, 2, TimeUnit.MINUTES)
        return redisTemplate.opsForValue().increment(key, 1)!! == 1L
    }

    fun unlock(name: String) {
        redisTemplate.delete(BANK_ACCOUNT_LOCK_KEY + name)
    }
}
