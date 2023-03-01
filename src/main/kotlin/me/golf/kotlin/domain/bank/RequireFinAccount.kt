package me.golf.kotlin.domain.bank

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireFinAccount(
    val type: LookupType
)
