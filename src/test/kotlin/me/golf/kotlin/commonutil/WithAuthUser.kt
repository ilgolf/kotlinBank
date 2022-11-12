package me.golf.kotlin.commonutil

import me.golf.kotlin.domain.member.model.RoleType
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithAuthUserSecurityContextFactory::class)
annotation class WithAuthUser(
    val id: Long = 1L,
    val email: String = "ilgolc@naver.com",
    val password: String = "1234",
    val roleType: RoleType = RoleType.USER
)
