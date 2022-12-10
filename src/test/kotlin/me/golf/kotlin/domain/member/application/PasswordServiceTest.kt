package me.golf.kotlin.domain.member.application

import io.mockk.every
import io.mockk.mockk
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.domain.member.util.TestPasswordEncoder
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.data.repository.findByIdOrNull

internal class PasswordServiceTest {

    private val memberRepository: MemberRepository = mockk()
    private val encoder = TestPasswordEncoder.init()
    private val passwordService = PasswordService(memberRepository, encoder)

    @Test
    @DisplayName("비밀번호를 parameter로 받아 변경한다.")
    fun updatePassword() {
        // given
        val member = GivenMember.toMember()
        val changePassword = "kk3h23"

        every { memberRepository.findByIdOrNull(any()) }.returns(member)

        // when
        passwordService.updatePassword(changePassword, member.id)

        // then
        assertThat(encoder.matches(changePassword, member.password)).isTrue
    }

    @Test
    @DisplayName("임시 비밀번호를 발행한다.")
    fun publishTempPassword() {
        // given
        val member = GivenMember.toMember()
        every { memberRepository.findByIdOrNull(any()) }.returns(member)

        // when
        val resultDto = passwordService.publishTempPassword(1L)

        // then
        assertThat(resultDto.tempPassword).isNotEqualTo(member.password)
    }
}