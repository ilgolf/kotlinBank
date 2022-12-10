package me.golf.kotlin.domain.member.sms.presentation

import me.golf.kotlin.domain.member.sms.application.SmsService
import me.golf.kotlin.domain.member.sms.dto.SendAuthNumberApiRequestDto
import me.golf.kotlin.domain.member.sms.dto.SmsAuthApiRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2/members/phone")
class SmsController(
    private val smsService: SmsService
) {

    @PostMapping("/auth-number")
    fun sendAuthNumber(@Valid @RequestBody sendAuthNumberApiRequestDto: SendAuthNumberApiRequestDto) =
        ResponseEntity.ok(smsService.sendAuthNumber(sendAuthNumberApiRequestDto.phoneNumber))

    @PostMapping
    fun authorizePhoneNumber(@Valid @RequestBody smsAuthApiRequestDto : SmsAuthApiRequestDto) =
        ResponseEntity.ok(
            smsService.authorizePhoneNumber(smsAuthApiRequestDto.phoneNumber, smsAuthApiRequestDto.authNumber))
}