package me.golf.kotlin.global.security

import me.golf.kotlin.domain.member.error.MemberNotFoundException
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.global.common.RedisPolicy
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    var memberRepository: MemberRepository
) : UserDetailsService {

    @Cacheable(value = [RedisPolicy.AUTH_KEY], key = "#memberId")
    override fun loadUserByUsername(memberId: String): CustomUserDetails =
        memberRepository.getDetailById(memberId.toLong())?: throw MemberNotFoundException(memberId.toLong())

    @Cacheable(value = [RedisPolicy.AUTH_KEY], key = "#email")
    fun loadUserByEmail(email: String) = memberRepository.getDetailByEmail(email)?: throw MemberNotFoundException()
}
