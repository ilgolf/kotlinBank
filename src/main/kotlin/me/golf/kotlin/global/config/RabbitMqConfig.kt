package me.golf.kotlin.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate


@EnableRabbit
@Configuration
@Profile("dev", "prd")
class RabbitMqConfig(
    @Value("\${spring.rabbitmq.host}") private val host: String,
    @Value("\${spring.rabbitmq.username}") private val username: String,
    @Value("\${spring.rabbitmq.port}") private val port: Int,
    @Value("\${spring.rabbitmq.password}") private val password: String
) {

    @Bean
    fun rabbitMqConnectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.host = host
        connectionFactory.username = username
        connectionFactory.port = port
        connectionFactory.setPassword(password)
        return connectionFactory;
    }

    @Bean
    fun rabbitTemplate(objectMapper: ObjectMapper): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(rabbitMqConnectionFactory())
        rabbitTemplate.messageConverter = messageConverter(objectMapper)
        return rabbitTemplate
    }

    @Bean
    fun messageConverter(objectMapper: ObjectMapper): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }

    @Bean
    fun rabbitListenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrentConsumers(1)
        factory.setMaxConcurrentConsumers(1)
        factory.setDefaultRequeueRejected(false)

        // Set up the retry policy
        val retryTemplate = RetryTemplate()
        val retryPolicy = SimpleRetryPolicy(5) // Set the maximum number of retry attempts
        val backOffPolicy = FixedBackOffPolicy()
        backOffPolicy.backOffPeriod = 60000 // Set the backoff period to 1 minute (60000 milliseconds)

        retryTemplate.setRetryPolicy(retryPolicy)
        retryTemplate.setBackOffPolicy(backOffPolicy)

        factory.setRetryTemplate(retryTemplate)

        return factory
    }
}