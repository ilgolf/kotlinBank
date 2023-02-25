package me.golf.kotlin.domain.member.presentation

import me.golf.kotlin.domain.member.application.MemberCommandService
import me.golf.kotlin.domain.member.application.MemberQueryService
import me.golf.kotlin.domain.member.dto.request.MemberApiSaveRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberApiSearchRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberApiUpdateRequestDto
import me.golf.kotlin.domain.member.dto.response.SimpleMemberResponse
import me.golf.kotlin.domain.member.error.MemberException
import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2")
class MemberController(
    private val memberCommandService: MemberCommandService,
    private val memberQueryService: MemberQueryService
) {

    @PostMapping("/public/members")
    fun save(@Valid @RequestBody requestDto: MemberApiSaveRequestDto): ResponseEntity<SimpleMemberResponse> {

        check(requestDto.isPhoneConfirm) { throw MemberException.PhoneConfirmDeniedException(requestDto.phoneNumber) }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(SimpleMemberResponse(memberCommandService.save(requestDto.toServiceDto()), true))
    }

    @GetMapping("/members/detail")
    fun getDetail(@AuthenticationPrincipal member: CustomUserDetails) =
        ResponseEntity.ok(memberQueryService.getDetail(member.memberId))

    @GetMapping("/members/search")
    fun search(memberApiSearchRequestDto: MemberApiSearchRequestDto?, pageable: Pageable) =
        ResponseEntity.ok(memberQueryService.search(memberApiSearchRequestDto!!.toServiceDto(), pageable))

    @PutMapping("/members")
    fun update(@Valid @RequestBody memberApiUpdateRequestDto: MemberApiUpdateRequestDto,
               @AuthenticationPrincipal customUserDetails: CustomUserDetails): ResponseEntity<Unit> {

        memberCommandService.update(memberApiUpdateRequestDto.toService(), customUserDetails.memberId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/members")
    fun delete(@AuthenticationPrincipal customUserDetails: CustomUserDetails): ResponseEntity<Unit> {

        memberCommandService.delete(customUserDetails.memberId)
        return ResponseEntity.noContent().build();
    }
}

