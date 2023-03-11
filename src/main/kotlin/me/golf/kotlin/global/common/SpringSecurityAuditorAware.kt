package me.golf.kotlin.global.common

import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpringSecurityAuditorAware: AuditorAware<Long> {

    companion object {
        const val USER_AUTHORITY = "ROLE_USER"
    }

    override fun getCurrentAuditor(): Optional<Long> {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: return Optional.empty()

        val isUser = authentication.authorities.contains(SimpleGrantedAuthority(USER_AUTHORITY))

        if (isUser) {
            val principal = authentication.principal as CustomUserDetails
            return Optional.of(principal.memberId)
        }

        return Optional.empty()
    }
}