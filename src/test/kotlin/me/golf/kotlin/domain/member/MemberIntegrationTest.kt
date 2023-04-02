package me.golf.kotlin.domain.member

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.kotlin.commonutil.IntegrationTest
import me.golf.kotlin.domain.member.dto.request.MemberApiSaveRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberApiUpdateRequestDto
import me.golf.kotlin.domain.member.model.Birthday
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.domain.member.util.GivenMember
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
class MemberIntegrationTest
@Autowired constructor(
    private val memberRepository: MemberRepository,
    private val objectMapper: ObjectMapper,
    private val tokenProvider: TokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : IntegrationTest() {

    var member: Member = GivenMember.toMember()
    var token: String = ""

    @BeforeEach
    fun init() {
        member = memberRepository.save(GivenMember.toMember())

        val userDetails = customUserDetailsService.loadUserByUsername(member.id.toString())

        val authToken = UsernamePasswordAuthenticationToken(userDetails, "")

        token = tokenProvider.createToken(member.id, authToken).accessToken
    }

    @Test
    @DisplayName("저장에 성공하는 통합 시나리오 테스트")
    fun saveSuccess() {
        // given
        val memberSaveDto = MemberApiSaveRequestDto(
            email = "nokt@google.co.kr",
            password = "1234@@@",
            passwordConfirm = "1234@@@",
            name = "노경태태",
            nickname = "noktnokt",
            birth = LocalDate.of(1999, 12, 21),
            profileImageUrl = "/Users/nogyeongtae/repository/basic.png",
            phoneNumber = "010-2133-5345",
            true)

        val body = objectMapper.writeValueAsString(memberSaveDto)

        // when
        val resultActions = mockMvc.perform(
            post("/api/v2/public/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isCreated)
            .andDo(print())
    }

    @Test
    @DisplayName("필수 값이 비어있으면 400 BadRequest")
    fun saveFailWithNull() {
        // given
        val memberSaveDto = MemberApiSaveRequestDto(
            email = "nokt@google.co.kr",
            password = "",
            passwordConfirm = "1234@@@",
            name = "",
            nickname = "noktnokt",
            birth = LocalDate.of(1999, 12, 21),
            profileImageUrl = "/Users/nogyeongtae/repository/basic.png",
            phoneNumber = "010-2133-5345",
            true)

        val body = objectMapper.writeValueAsString(memberSaveDto)

        // when
        val resultActions = mockMvc.perform(
            post("/api/v2/public/members")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isBadRequest)
            .andDo(print())
    }

    @Test
    @DisplayName("업데이트 통합 시나리오 테스트")
    fun updateSuccess() {
        // given
        val requestDto = MemberApiUpdateRequestDto(
            name = "김시온",
            nickname = "마일드시온c",
            profileImage = GivenMember.profileImage.value,
            birth = Birthday(LocalDate.of(1998, 11, 1)).value
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val resultActions = mockMvc.perform(
            put("/api/v2/members")
                .header("Authorization", "Bearer $token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("필드 중 하나라도 빈 값이면 400 badRequest")
    fun updateFailByFieldError() {
        // given
        val requestDto = MemberApiUpdateRequestDto(
            name = "",
            nickname = "마일드시온c",
            profileImage = GivenMember.profileImage.value,
            birth = Birthday(LocalDate.of(1998, 11, 1)).value
        )

        val body = objectMapper.writeValueAsString(requestDto)

        // when
        val resultActions = mockMvc.perform(
            put("/api/v2/members")
                .header("Authorization", "Bearer $token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isBadRequest)
            .andDo(print())
    }
    
    @Test
    @DisplayName("상세조회 통합 시나리오 테스트")
    fun getMemberDetailSuccess() {
        // given

        // when
        val resultActions = mockMvc.perform(
            get("/api/v2/members/detail")
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("회원 목록 조회 통합 시나리오 테스트")
    fun getMemberListSuccess() {
        // given

        // when
        val resultActions = mockMvc.perform(
            get("/api/v2/members/search")
                .param("email", GivenMember.email.value)
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("잘못된 메서드로 요청이 들어오면 405")
    fun methodNotSupported() {
        // given

        // when
        val resultActions = mockMvc.perform(
            post("/api/v2/members/search")
                .param("email", GivenMember.email.value)
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        resultActions.andExpect(status().isMethodNotAllowed)
            .andDo(print())
    }

    @Test
    @DisplayName("회원 삭제 통합 시나리오 테스트")
    fun deleteMember() {
        // given

        // when
        val resultActions = mockMvc.perform(
            delete("/api/v2/members")
                .header("Authorization", "Bearer $token")
        )

        // then
        resultActions.andExpect(status().isNoContent)
            .andDo(print())
    }

    @Test
    @DisplayName("회원 삭제는 비회원은 불가능하다.")
    fun deleteFailByUnAuthorize() {
        // given

        // when
        val resultActions = mockMvc.perform(
            delete("/api/v2/members")
        )

        // then
        resultActions.andExpect(status().isUnauthorized)
            .andDo(print())
    }
}
