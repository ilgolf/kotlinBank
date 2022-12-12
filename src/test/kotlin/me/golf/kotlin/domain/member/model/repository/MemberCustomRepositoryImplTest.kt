package me.golf.kotlin.domain.member.model.repository

import me.golf.kotlin.commonutil.JpaTest
import me.golf.kotlin.domain.member.dto.request.MemberSearchRequestDto
import me.golf.kotlin.domain.member.error.MemberNotFoundException
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.UserEmail
import me.golf.kotlin.domain.member.util.GivenMember
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.test.context.support.WithMockUser

internal class MemberCustomRepositoryImplTest

@Autowired constructor(
    private val memberRepository: MemberRepository
): JpaTest() {

    companion object {
        var member: Member = GivenMember.toMember()
    }

    @BeforeEach
    @WithMockUser
    fun init() {
        member = memberRepository.save(GivenMember.toMember())
    }

    @Test
    @DisplayName("database 테스트 : 사용자 인증정보를 DB에서 가져온다.")
    fun getDetailById() {
        // given

        // when
        val customUserDetails = memberRepository.getDetailById(member.id)
            ?: throw MemberNotFoundException(member.id)

        // then
        assertThat(customUserDetails.memberId).isEqualTo(member.id)
    }

    @Test
    @DisplayName("이메일로 검색하여 회원의 간략한 정보를 조회할 수 있다.")
    fun getShortByEmail() {
        // given
        val requestDto = MemberSearchRequestDto("ilgolc@", null)

        // when
        val memberResponses = memberRepository.findAllBySearchDto(requestDto, PageRequest.of(0, 10))

        // then
        assertThat(memberResponses.content.size).isEqualTo(1)
    }

    @Test
    @DisplayName("닉네임으로 검색하여 회원의 간략한 정보를 조회할 수 있다.")
    fun getShortByNickname() {
        // given
        val requestDto = MemberSearchRequestDto(null, "nok")

        // when
        val memberResponses = memberRepository.findAllBySearchDto(requestDto, PageRequest.of(0, 10)).content

        // then
        assertThat(memberResponses.size).isEqualTo(1)
    }

    @Test
    @DisplayName("이메일로 회원인증 정보를 가져온다.")
    fun getDetailByEmail() {
        // given
        val email = GivenMember.email.value

        // when
        val userDetails = memberRepository.getDetailByEmail(email)

        // then
        assertThat(userDetails?.email).isEqualTo(UserEmail(email))
    }
}