package me.golf.kotlin.domain.member.model

import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.domain.member.util.TestPasswordEncoder
import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.*
import java.time.LocalDate

internal class MemberTest {

    @Test
    fun updateMember() {
        // given
        val member = GivenMember.toMember()
        val name = "name"
        val nickname = "nickname"
        val birth = LocalDate.now()
        val profileImage = "/Users/nokt/Desktop/image.png"

        // when
        val updateMember = member.updateMember(nickname, name, birth, profileImage)

        // then
        assertThat(updateMember.nickname).isEqualTo(nickname)
        assertThat(updateMember.name).isEqualTo(name)
        assertThat(updateMember.birth).isEqualTo(Birthday(birth))
        assertThat(updateMember.profileImage).isEqualTo(ProfileImage(profileImage))
    }

    @Test
    fun matchPassword() {
        // given
        val encoder = TestPasswordEncoder.init()
        val member = GivenMember.toMember().encodePassword(encoder)
        val originPassword = GivenMember.password

        // when
        val isMatch = member.matchPassword(encoder, originPassword)

        // then
        assertThat(isMatch).isTrue
    }

    @Test
    fun matchPasswordFail() {
        // given
        val encoder = TestPasswordEncoder.init()
        val member = GivenMember.toMember().encodePassword(encoder)
        val originPassword = "123214312423"

        // when
        val isMatch = member.matchPassword(encoder, originPassword)

        // then
        assertThat(isMatch).isFalse
    }

    @Test
    fun encodePassword() {
        // given
        val encoder = TestPasswordEncoder.init()
        val member = GivenMember.toMember()
        val originPassword = member.password

        // when
        val encodeMember = member.encodePassword(encoder)

        // then
        assertThat(encodeMember.password).isNotEqualTo(originPassword)
    }

    @Test
    fun getEmailId() {
        // given
        val member = GivenMember.toMember()

        // when
        val emailId = member.getEmailId();

        // then
        assertThat(emailId).isEqualTo("ilgolc")
    }

    @Test
    fun validateExceedDate() {
        // given
        val member = GivenMember.toMember()
        val exceededBirth = LocalDate.of(2023, 12, 31)

        // when
        val isExceed = member.validateExceedDate(exceededBirth)

        // then
        assertThat(isExceed).isTrue
    }

    @Test
    fun getAge() {
        // given
        val member = GivenMember.toMember()

        // when
        val age = member.getAge()

        // then
        assertThat(age).isEqualTo(26)
    }

    @Test
    fun delete() {
        // given
        val member = GivenMember.toMember()

        // when
        val deletedMember = member.delete()

        // then
        assertThat(deletedMember.deleted).isTrue
    }

    @Test
    fun changePassword() {
        // given
        val encoder = TestPasswordEncoder.init()
        val member = GivenMember.toMember()

        // when
        val changePasswordMember = member.changePassword(encoder, "3456")

        // then
        changePasswordMember.matchPassword(encoder, "3456")
    }

    @Test
    fun validationProfileUrl() {
        // given
        val member = GivenMember.toMember()
        member.profileImage = ProfileImage("/User/nokt/program/image.png")

        // when
        val isProfileUrl = member.validationProfileUrl(member.profileImage)

        // then
        assertThat(isProfileUrl).isTrue
    }

    @Test
    fun equalTest() {
        // given
        val member1 = GivenMember.toMember()
        member1.id = 4L

        val member2 = Member(
            email = UserEmail("nokt@naver.com"),
            password = "3234",
            name = "김딱구",
            nickname = "응애",
            birth = Birthday(LocalDate.of(1996, 11, 2)),
            RoleType.USER,
            profileImage = GivenMember.profileImage,
            phoneNumber = "010-2344-1233"
        )
        member2.id = 4L

        // when, then
        assertThat(member1).isEqualTo(member2)
    }
}