package me.golf.kotlin.domain.member.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.member.dto.request.MemberSearchRequestDto
import me.golf.kotlin.domain.member.dto.response.MemberApiShortResponseDto
import me.golf.kotlin.domain.member.error.MemberException
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.UserEmail
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

internal class MemberQueryServiceTest {

    private val memberRepository: MemberRepository = mockk()
    private val memberQueryService = MemberQueryService(memberRepository)

    @Test
    @DisplayName("회원 식별자를 받아 회원 개인정보를 조회한다.")
    fun getDetailSuccess() {
        // given
        val member = GivenMember.toMember()
        member.id = 2L

        every { memberRepository.findById(any()) } returns Optional.of(member)

        // when
        val memberResponse = memberQueryService.getDetail(memberId = member.id)

        // then
        assertThat(memberResponse.email).isEqualTo(member.email)
    }

    @Test
    @DisplayName("회원 식별자가 없으면 가져오지 못한다.")
    fun getDetailFailByNotFound() {
        // given
        every { memberRepository.findById(any()) } returns Optional.empty()

        // when
        val exception = catchException { memberQueryService.getDetail(2L) }

        // then
        assertThat(exception).isInstanceOf(MemberException.MemberNotFoundException::class.java)
    }

    @Test
    @DisplayName("검색 키워드가 있으면 검색한 데이터를 받아온다.")
    fun search() {
        // given
        val members = ArrayList<Member>()

        for (i in 1 .. 5) {
            val member = GivenMember.toMember()
            member.email = UserEmail("email${i}@naver.com")
            member.id = i.toLong()

            members.add(member)
        }

        val memberPageable = PageImpl(members, PageRequest.of(0, 10), 5)
            .map { MemberApiShortResponseDto.of(it) }

        val searchDto = MemberSearchRequestDto("email", null)

        every { memberRepository.findAllBySearchDto(any(), any()) } returns memberPageable

        // when
        val memberPage = memberQueryService.search(searchDto, PageRequest.of(0, 10))

        // then
        assertThat(memberPage.data.size).isEqualTo(5)
    }

    @Test
    @DisplayName("검색 키워드가 없으면 빈 페이지를 반환한다.")
    fun searchFail() {
        // given
        val searchRequestDto: MemberSearchRequestDto? = null

        // when
        val page = memberQueryService.search(searchRequestDto, PageRequest.of(0, 10))

        // then
        assertThat(page.data).isEmpty()
    }
}