package me.golf.kotlin.global.security

import me.golf.kotlin.domain.member.error.MemberNotFoundException
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    var memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(memberId: String): UserDetails {

        return memberRepository.findById(memberId.toLong())
            .map { CustomUserDetails.of(it) }
            .orElseThrow { throw MemberNotFoundException(memberId.toLong()) }
    }
}
