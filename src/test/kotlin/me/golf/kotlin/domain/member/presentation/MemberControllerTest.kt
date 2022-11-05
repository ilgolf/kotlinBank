package me.golf.kotlin.domain.member.presentation

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.member.application.MemberCommandService
import me.golf.kotlin.domain.member.application.MemberQueryService
import me.golf.kotlin.domain.member.dto.request.MemberApiSaveRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberApiSearchRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberApiUpdateRequestDto
import me.golf.kotlin.domain.member.dto.response.MemberApiDetailDto
import me.golf.kotlin.domain.member.dto.response.MemberApiShortResponseDto
import me.golf.kotlin.domain.member.model.Birthday
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.UserEmail
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.common.PageCustomResponse
import me.golf.kotlin.global.security.CustomUserDetails
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.stream.Collectors

internal class MemberControllerTest {

    private val memberQueryService: MemberQueryService = mockk()
    private val memberCommandService: MemberCommandService = mockk()
    private val memberController = MemberController(memberCommandService, memberQueryService)

    @Test
    @DisplayName("회원 생성 시 상태코드 201 created를 반환한다.")
    fun save() {
        // given
        val requestDto = MemberApiSaveRequestDto(
            email = GivenMember.email.value,
            name = GivenMember.name,
            password = GivenMember.password,
            nickname = GivenMember.nickname,
            phoneNumber = GivenMember.phoneNumber,
            birth = GivenMember.birth.value,
            profileImageUrl = GivenMember.profileImage.value,
            passwordConfirm = GivenMember.password
        )

        every { memberCommandService.save(any()) } returns 2L

        // when
        val response = memberController.save(requestDto)

        // then
        assertAll(
            { response.body?.let { assertThat(it.memberId).isEqualTo(2L) } },
            { response.body?.let { assertThat(it.result).isTrue } }
        )
    }

    @Test
    @DisplayName("상세 조회 시 상태코드 200을 반환한다.")
    fun getDetail() {
        // given
        val member = GivenMember.toMember()
        val memberResponse = MemberApiDetailDto.of(member)
        val customUserDetails = CustomUserDetails.of(member)

        every { memberQueryService.getDetail(any()) } returns memberResponse

        // when
        val detailResponse = memberController.getDetail(customUserDetails)

        // then
        assertThat(detailResponse.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("회원 검색 시 상태코드 200을 반환한다.")
    fun search() {
        // given
        val members = ArrayList<Member>()
        val searchRequestDto = MemberApiSearchRequestDto(
            email = GivenMember.email.value,
            nickname = GivenMember.nickname
        )

        for (i in 1 .. 5) {
            val member = GivenMember.toMember()
            member.email = UserEmail("email${i}@naver.com")
            member.id = i.toLong()

            members.add(member)
        }

        val memberResponses = members.stream()
            .map { MemberApiShortResponseDto.of(it) }
            .collect(Collectors.toList())

        val page = PageImpl(memberResponses, PageRequest.of(0, 10), 5)

        every { memberQueryService.search(any(), any()) } returns PageCustomResponse.of(page)

        // when
        val response = memberController.search(searchRequestDto, PageRequest.of(0, 10))

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("회원 수정 시 200 상태코드를 반환한다.")
    fun update() {
        // given
        val member = GivenMember.toMember()
        val requestDto = MemberApiUpdateRequestDto(
            name = "updateName",
            nickname = "updateNickname",
            profileImage = GivenMember.profileImage,
            birthday = Birthday(LocalDate.of(1998, 11, 2))
        )

        member.id = 2L

        every { memberCommandService.update(any(), any()) } returns Unit

        // when
        val updateMember = memberController.update(requestDto, CustomUserDetails.of(member))

        // then
        assertThat(updateMember.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @DisplayName("회원 삭제 요청 시 204 상태코드를 반환한다.")
    fun delete() {
        // given
        val customUserDetails = CustomUserDetails.of(GivenMember.toMember())
        every { memberCommandService.delete(any()) } returns Unit

        // when
        val response = memberController.delete(customUserDetails = customUserDetails)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }
}