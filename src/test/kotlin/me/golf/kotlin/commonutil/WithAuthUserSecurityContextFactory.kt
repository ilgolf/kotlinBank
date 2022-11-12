package me.golf.kotlin.commonutil

import me.golf.kotlin.domain.member.model.RoleType
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithAuthUserSecurityContextFactory: WithSecurityContextFactory<WithAuthUser> {

    override fun createSecurityContext(annotation: WithAuthUser?): SecurityContext {
        val member = GivenMember.toMember()

        val role = AuthorityUtils.createAuthorityList(RoleType.USER.name)

        val userDetails = CustomUserDetails.of(member)

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(userDetails, "", role)

        return SecurityContextHolder.getContext()
    }
}
