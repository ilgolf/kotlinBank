package me.golf.kotlin.domain.member.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.member.dto.request.MemberSaveRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberUpdateRequestDto
import me.golf.kotlin.domain.member.error.DuplicateEmailException
import me.golf.kotlin.domain.member.error.DuplicateNicknameException
import me.golf.kotlin.domain.member.error.MemberNotFoundException
import me.golf.kotlin.domain.member.model.ProfileImage
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import java.util.*

internal class MemberCommandServiceTest {

    private val memberRepository: MemberRepository = mockk()
    private val memberCommandService = MemberCommandService(memberRepository)

    @Test
    @DisplayName("회원을 저장한다.")
    fun saveSuccess() {
        // given
        val reqDto = MemberSaveRequestDto(
            email = GivenMember.email.value,
            password = GivenMember.password,
            name = GivenMember.name,
            nickname = GivenMember.nickname,
            birth = GivenMember.birth.value,
            profileImageUrl = GivenMember.profileImage.value,
            phoneNumber = GivenMember.phoneNumber
        )
        val member = GivenMember.toMember()
        member.id = 2L

        every { memberRepository.existsByEmail(any()) } returns false
        every { memberRepository.existsByNickname(any()) } returns false
        every { memberRepository.save(any()) } returns member

        // when
        val memberId = memberCommandService.save(reqDto)

        // then
        assertThat(memberId).isEqualTo(2L)
    }

    @Test
    @DisplayName("이메일 또는 닉네임이 중복이면 가입에 실패한다.")
    fun saveFailByDuplication() {
        // given
        val reqDto = MemberSaveRequestDto(
            email = GivenMember.email.value,
            password = GivenMember.password,
            name = GivenMember.name,
            nickname = GivenMember.nickname,
            birth = GivenMember.birth.value,
            profileImageUrl = GivenMember.profileImage.value,
            phoneNumber = GivenMember.phoneNumber
        )
        val member = GivenMember.toMember()
        member.id = 2L

        every { memberRepository.existsByEmail(any()) } returns true
        every { memberRepository.existsByNickname(any()) } returns true
        every { memberRepository.save(any()) } returns member

        // when
        val exception = catchException { memberCommandService.save(reqDto) }

        // then
        assertThat(exception).isInstanceOf(DuplicateEmailException::class.java)
    }

    @Test
    @DisplayName("updateRequestDto를 기반으로 회원 정보가 수정된다.")
    fun updateSuccess() {
        // given
        val reqDto = MemberUpdateRequestDto(
            name = "updateName",
            nickname = "updateNickname",
            profileImage = ProfileImage("https://www.test-url.com/image"),
            birthday = GivenMember.birth
        )
        val member = GivenMember.toMember()
        member.id = 2L

        every { memberRepository.findById(any()) } returns Optional.of(member)
        every { memberRepository.existsByNickname(any()) } returns false

        // when
        memberCommandService.update(reqDto, member.id)

        // then
        assertAll(
            { assertThat(member.name).isEqualTo("updateName") },
            { assertThat(member.nickname).isEqualTo("updateNickname") },
            { assertThat(member.profileImage.value).isEqualTo("https://www.test-url.com/image") }
        )
    }

    @Test
    @DisplayName("닉네임이 중복이면 회원 정보 수정이 불가능하다.")
    fun updateFailByDuplication() {
        // given
        val reqDto = MemberUpdateRequestDto(
            name = "updateName",
            nickname = "updateNickname",
            profileImage = ProfileImage("https://www.test-url.com/image"),
            birthday = GivenMember.birth
        )
        val member = GivenMember.toMember()
        member.id = 2L

        every { memberRepository.findById(any()) } returns Optional.of(member)
        every { memberRepository.existsByNickname(any()) } returns true

        // when
        val exception = catchException { memberCommandService.update(reqDto, memberId = member.id) }

        // then
        assertThat(exception).isInstanceOf(DuplicateNicknameException::class.java)
    }

    @Test
    @DisplayName("회원 탈퇴하면 deleted가 true이다.")
    fun deleteSuccess() {
        // given
        val member = GivenMember.toMember()
        member.id = 2L

        every { memberRepository.findById(any()) } returns Optional.of(member)

        // when
        memberCommandService.delete(memberId = member.id)

        // then
        assertThat(member.deleted).isTrue
    }

    @Test
    @DisplayName("없는 회원이면 예외가 발생하고 탈퇴에 실패한다.")
    fun deletedFailByNotFound() {
        // given
        every { memberRepository.findById(any()) } returns Optional.ofNullable(null)

        // when
        val exception = catchException { memberCommandService.delete(3L) }

        // then
        assertThat(exception).isInstanceOf(MemberNotFoundException::class.java)
    }
}