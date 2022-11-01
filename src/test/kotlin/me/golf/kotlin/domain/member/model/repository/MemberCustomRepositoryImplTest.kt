package me.golf.kotlin.domain.member.model.repository

import me.golf.kotlin.commonutil.JpaTest
import me.golf.kotlin.domain.member.error.MemberNotFoundException
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.util.GivenMember
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

internal class MemberCustomRepositoryImplTest

@Autowired constructor(
    private val memberRepository: MemberRepository
): JpaTest() {

    companion object {
        var member: Member = GivenMember.toMember()
    }

    @BeforeEach
    fun init() {
        member = memberRepository.save(GivenMember.toMember())
    }

    @Test
    fun getDetailById() {
        // given

        // when
        val customUserDetails = memberRepository.getDetailById(member.id)
            .orElseThrow { throw MemberNotFoundException(member.id) }

        // then
        assertThat(customUserDetails.memberId).isEqualTo(member.id)
    }
}