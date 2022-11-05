package me.golf.kotlin.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.kotlin.global.common.RedisPolicy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig(
    @Value("\${spring.redis.host}") private val host: String,
    @Value("\${spring.redis.port}") private val port: Int,
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(host, port)

    @Bean
    fun redisCacheManager(): RedisCacheManager {
        val configuration = RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(30))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                StringRedisSerializer()
            ))

        val config = HashMap<String, RedisCacheConfiguration>()

        config[RedisPolicy.AUTH_KEY] = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(RedisPolicy.AUTH_TTL))

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .cacheDefaults(configuration)
            .withInitialCacheConfigurations(config)
            .build()
    }
}