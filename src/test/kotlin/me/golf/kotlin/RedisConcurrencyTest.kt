package me.golf.kotlin

import me.golf.kotlin.commonutil.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RedisConcurrencyTest
@Autowired
constructor(
    private val redisTemplate: RedisTemplate<String, Int>,
) : IntegrationTest() {

    private val log = LoggerFactory.getLogger(RedisConcurrencyTest::class.java)
    lateinit var executorService: ExecutorService
    lateinit var template: HashOperations<String, String, Int>

    @BeforeEach
    fun init() {
        executorService = Executors.newFixedThreadPool(5)
        template = redisTemplate.opsForHash()
    }

    @AfterEach
    fun destroy() {
        template.delete("hash", "key")
    }

    @Test
    fun concurrency() {
        // given
        val latch = CountDownLatch(30)

        // when
        for (i in 1..30) {
            executorService.execute {

                val value = template.increment("hash", "key", 1).toInt()

                if (value > 1) {
                    log.error("함수 실행 실패 !!!!")
                    latch.countDown()
                    return@execute
                }

                log.info("함수 실행 !!!!")

                latch.countDown()
            }
        }

        latch.await()

        // then
        assertThat(template["hash", "key"]).isEqualTo(30)
    }
}