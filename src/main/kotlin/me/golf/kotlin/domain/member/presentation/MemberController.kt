package me.golf.kotlin.domain.member.presentation

import me.golf.kotlin.domain.member.application.MemberCommandService
import me.golf.kotlin.domain.member.application.MemberQueryService
import me.golf.kotlin.domain.member.dto.request.MemberApiSaveRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberApiSearchRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberApiUpdateRequestDto
import me.golf.kotlin.domain.member.dto.response.MemberApiDetailDto
import me.golf.kotlin.domain.member.dto.response.MemberApiShortResponseDto
import me.golf.kotlin.domain.member.dto.response.SimpleMemberResponse
import me.golf.kotlin.global.common.PageCustomResponse
import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v2")
class MemberController(
    private val memberCommandService: MemberCommandService,
    private val memberQueryService: MemberQueryService
) {

    @PostMapping("/public/members")
    fun save(@RequestBody memberApiSaveRequestDto: MemberApiSaveRequestDto): ResponseEntity<SimpleMemberResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(SimpleMemberResponse(memberCommandService.save(memberApiSaveRequestDto.toServiceDto()), true))
    }

    @GetMapping("/members/detail")
    fun getDetail(@AuthenticationPrincipal member: CustomUserDetails): ResponseEntity<MemberApiDetailDto> {
        return ResponseEntity.ok(memberQueryService.getDetail(member.memberId))
    }

    @GetMapping("/members/search")
    fun search(memberApiSearchRequestDto: MemberApiSearchRequestDto?,
               pageable: Pageable): ResponseEntity<PageCustomResponse<MemberApiShortResponseDto>> {

        val dto = memberApiSearchRequestDto?.toServiceDto()

        return ResponseEntity.ok(memberQueryService.search(dto, pageable))
    }

    @PutMapping("/members")
    fun update(memberApiUpdateRequestDto: MemberApiUpdateRequestDto,
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