package me.golf.kotlin.global.config

import me.golf.kotlin.global.common.PaymentMqPolicy.PAYMENT_DEAD_LETTER_QUEUE
import me.golf.kotlin.global.common.PaymentMqPolicy.PAYMENT_QUEUE
import me.golf.kotlin.global.common.PaymentMqPolicy.REFUND_DEAD_LETTER_QUEUE
import me.golf.kotlin.global.common.PaymentMqPolicy.REFUND_QUEUE
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@EnableRabbit
@Configuration
@Profile("dev", "prd")
class RabbitProducerConfig(
    @Value("\${message.queue.topic-key}") private val topicKey: String,
    @Value("\${message.queue.dead-topic-key}") private val deadTopicKey: String
) {

    @Bean
    fun paymentQueue(): Queue {
        return QueueBuilder.durable(PAYMENT_QUEUE)
            .withArgument("x-dead-letter-exchange", deadTopicKey)
            .withArgument("x-dead-letter-routing-key", PAYMENT_DEAD_LETTER_QUEUE)
            .build()
    }

    @Bean
    fun refundQueue(): Queue {
        return QueueBuilder.durable(REFUND_QUEUE)
            .withArgument("x-dead-letter-exchange", deadTopicKey)
            .withArgument("x-dead-letter-routing-key", REFUND_DEAD_LETTER_QUEUE)
            .build()
    }


    @Bean
    fun deadLetterQueuePayment() = Queue("deadLetterPayment", true)

    @Bean
    fun deadLetterQueueRefund() = Queue("deadLetterRefund", true)

    @Bean
    fun topicExchange(): TopicExchange {
        return TopicExchange(topicKey)
    }

    @Bean
    fun deadTopicKey(): TopicExchange {
        return TopicExchange(deadTopicKey)
    }

    @Bean
    fun paymentQueueBinding(): Binding =
        BindingBuilder.bind(paymentQueue()).to(topicExchange()).with("payment.#")

    @Bean
    fun refundQueueBinding(): Binding =
        BindingBuilder.bind(refundQueue()).to(topicExchange()).with("refund.#")

    @Bean
    fun deadLetterBindingPayment(): Binding =
        BindingBuilder.bind(deadLetterQueuePayment()).to(deadTopicKey()).with("dead.payment.#")

    @Bean
    fun deadLetterBindingRefund(): Binding =
        BindingBuilder.bind(deadLetterQueueRefund()).to(deadTopicKey()).with("dead.refund.#")
}