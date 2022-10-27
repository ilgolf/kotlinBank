package me.golf.kotlin.domain.member.model.repository;

import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.UserEmail
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

    fun existsByEmail(email: UserEmail): Boolean

    fun existsByNickname(nickname: String): Boolean
}