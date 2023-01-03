package me.golf.kotlin.commonutil

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class TestContainer {

    companion object {
        private const val REDIS_IMAGE = "redis:latest"

        @JvmStatic
        @Container
        protected val REDIS_CONTAINER: GenericContainer<Nothing> = GenericContainer<Nothing>(REDIS_IMAGE)
            .apply { withExposedPorts(6379) }
            .apply { withReuse(true) }
            .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.redis.host", REDIS_CONTAINER::getHost)
            registry.add("spring.redis.port", REDIS_CONTAINER::getFirstMappedPort)
        }
    }
}

