package me.golf.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
class KotlinApplication

fun main(args: Array<String>) {
	runApplication<KotlinApplication>(*args)
}
