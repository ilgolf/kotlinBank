package me.golf.kotlin.domain.bank.model

import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_REGISTER_NUMBER
import me.golf.kotlin.global.common.RedisPolicy
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class BankAccountRedisRepository(
    private val stringRedisTemplate: StringRedisTemplate,
) {
    fun findBalancesByFinAccount(finAccounts: List<String>): Map<String, String> {
        val opsForHash = stringRedisTemplate.opsForHash<String, String>()
        val balances = opsForHash.multiGet(RedisPolicy.BANK_ACCOUNT_KEY, finAccounts)

        val balanceToFinAccountMap = mutableMapOf<String, String>()

        if (balances.filterNotNull().isEmpty()) {
            for (i in finAccounts.indices) {
                balanceToFinAccountMap[finAccounts[i]] = DEFAULT_REGISTER_NUMBER
            }

            return balanceToFinAccountMap
        }

        for (i in finAccounts.indices) {
            balanceToFinAccountMap[finAccounts[i]] = balances[i]
        }

        return balanceToFinAccountMap
    }

    fun findBalanceByFinAccount(finAccount: String): String? {
        return stringRedisTemplate.opsForHash<String, String>().get(RedisPolicy.BANK_ACCOUNT_KEY, finAccount)
    }
}
