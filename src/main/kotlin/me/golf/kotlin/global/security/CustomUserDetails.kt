package me.golf.kotlin.global.security

import com.querydsl.core.annotations.QueryProjection
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.RoleType
import me.golf.kotlin.domain.member.model.UserEmail
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Collections

class CustomUserDetails

@QueryProjection
constructor(
    var memberId: Long,
    var email: UserEmail,
    var roleType: RoleType
) : UserDetails {

    companion object {
        fun of(member: Member): CustomUserDetails {
            return CustomUserDetails(memberId = member.id, email = member.email, roleType = member.roleType)
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singleton(SimpleGrantedAuthority("ROLE_${roleType.name}"))
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return email.value
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}