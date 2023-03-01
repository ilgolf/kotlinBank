package me.golf.kotlin.domain.bank.presentation

import me.golf.kotlin.domain.bank.LookupType
import me.golf.kotlin.domain.bank.RequireFinAccount
import me.golf.kotlin.domain.bank.application.BankAccountCommandService
import me.golf.kotlin.domain.bank.application.BankAccountQueryService
import me.golf.kotlin.domain.bank.dto.BankAccountInfoResponseDto
import me.golf.kotlin.domain.bank.dto.BankAccountSaveApiRequestDto
import me.golf.kotlin.domain.bank.dto.BankAccountUpdateRequestDto
import me.golf.kotlin.domain.bank.dto.SimpleBankAccountIdResponseDto
import me.golf.kotlin.domain.bank.history.application.TransferHistoryService
import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2/bank-accounts")
class BankAccountController(
    private val bankAccountCommandService: BankAccountCommandService,
    private val bankAccountQueryService: BankAccountQueryService,
    private val transferHistoryService: TransferHistoryService
) {

    @PostMapping
    fun save(
        @Valid @RequestBody reqDto: BankAccountSaveApiRequestDto,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<SimpleBankAccountIdResponseDto> {

        return ResponseEntity.ok(bankAccountCommandService.save(reqDto.toServiceDto(customUserDetails.memberId)))
    }

    @GetMapping("/{bankAccountId}")
    fun getBankAccountSummary(
        @PathVariable bankAccountId: Long,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PageableDefault pageable: Pageable
    ): ResponseEntity<BankAccountInfoResponseDto> {

        val bankAccountSummaryResponseDto =
            bankAccountQueryService.getBankAccountSummary(bankAccountId, customUserDetails.memberId)
        val historyResponseDto =
            transferHistoryService.getHistories(bankAccountId, customUserDetails.memberId, pageable)

        return ResponseEntity.ok(BankAccountInfoResponseDto(bankAccountSummaryResponseDto, historyResponseDto))
    }

    @GetMapping
    fun getBankAccountsByMemberId(@AuthenticationPrincipal customUserDetails: CustomUserDetails) =
        ResponseEntity.ok(bankAccountQueryService.getBankAccountsByMemberId(customUserDetails.memberId))

    @PatchMapping("/balance/{bankAccountId}")
    @RequireFinAccount(type = LookupType.ONE)
    fun updateBalance(@AuthenticationPrincipal customUserDetails: CustomUserDetails,
                      @PathVariable bankAccountId: Long) =

        bankAccountQueryService.getBalance(bankAccountId, customUserDetails.memberId)

    @PatchMapping("/{bankAccountId}")
    fun update(
        @Valid @RequestBody requestDto: BankAccountUpdateRequestDto,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable bankAccountId: Long
    ): ResponseEntity<Unit> {

        bankAccountCommandService.update(requestDto.nickname, bankAccountId, customUserDetails.memberId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{bankAccountId}")
    fun delete(
        @PathVariable bankAccountId: Long,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<Unit> {

        bankAccountCommandService.delete(bankAccountId, customUserDetails.memberId)
        return ResponseEntity.noContent().build()
    }
}
