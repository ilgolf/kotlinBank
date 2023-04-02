package me.golf.kotlin.global.config

import me.golf.kotlin.domain.bank.payment.application.PaymentProcessor
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@EnableRabbit
@Configuration
@Profile("dev", "prd")
class RabbitConsumerConfig(
    private val paymentProcessor: PaymentProcessor
) {

    @Bean
    fun paymentListenerContainer(
        connectionFactory: ConnectionFactory,
        paymentQueue: Queue,
        paymentListenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {

        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueues(paymentQueue)
        container.setMessageListener(paymentListenerAdapter)
        return container
    }

    @Bean
    fun refundListenerContainer(
        connectionFactory: ConnectionFactory,
        refundQueue: Queue,
        refundListenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {

        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueues(refundQueue)
        container.setMessageListener(refundListenerAdapter)
        return container
    }

    @Bean
    fun paymentListenerAdapter(messageConverter: MessageConverter): MessageListenerAdapter {
        val listenerAdapter = MessageListenerAdapter(paymentProcessor, "subscribePayment")
        listenerAdapter.setMessageConverter(messageConverter)
        listenerAdapter.setDefaultListenerMethod("subscribePayment")
        return listenerAdapter
    }

    @Bean
    fun refundListenerAdapter(messageConverter: MessageConverter): MessageListenerAdapter {
        val listenerAdapter = MessageListenerAdapter(paymentProcessor, "subscribeRefund")
        listenerAdapter.setMessageConverter(messageConverter)
        listenerAdapter.setDefaultListenerMethod("subscribeRefund")
        return listenerAdapter
    }
}