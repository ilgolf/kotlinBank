package me.golf.kotlin.domain.member.presentation

import me.golf.kotlin.domain.member.application.PasswordService
import me.golf.kotlin.domain.member.dto.PasswordApiFindPasswordRequestDto
import me.golf.kotlin.domain.member.dto.PasswordApiUpdateRequestDto
import me.golf.kotlin.domain.member.dto.TempPasswordApiResponseDto
import me.golf.kotlin.domain.member.error.PhoneConfirmDeniedException
import me.golf.kotlin.domain.member.error.PhoneNumberMissMatchException
import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2/members/password")
class PasswordController(
    private val passwordService: PasswordService
) {

    @PutMapping
    fun updatePassword(
        @Valid @RequestBody requestDto: PasswordApiUpdateRequestDto,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<Unit> {

        check(requestDto.validPassword()) { throw PhoneNumberMissMatchException(requestDto.password) }

        passwordService.updatePassword(requestDto.password, customUserDetails.memberId)
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun publishTempPassword(
        @Valid @RequestBody requestDto: PasswordApiFindPasswordRequestDto,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<TempPasswordApiResponseDto> {

        check(requestDto.validPhone) { throw PhoneConfirmDeniedException(requestDto.phoneNumber) }
        return ResponseEntity.ok(passwordService.publishTempPassword(customUserDetails.memberId))
    }
}