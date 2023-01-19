package me.golf.kotlin.domain.bank.presentation

import me.golf.kotlin.domain.bank.BalanceResponseDto
import me.golf.kotlin.domain.bank.application.BankAccountCommandService
import me.golf.kotlin.domain.bank.application.BankAccountQueryService
import me.golf.kotlin.domain.bank.dto.BankAccountInfoResponseDto
import me.golf.kotlin.domain.bank.dto.BankAccountSaveApiRequestDto
import me.golf.kotlin.domain.bank.dto.BankAccountUpdateRequestDto
import me.golf.kotlin.domain.bank.dto.SimpleBankAccountIdResponseDto
import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2/bank-accounts")
class BankAccountController(
    private val bankAccountCommandService: BankAccountCommandService,
    private val bankAccountQueryService: BankAccountQueryService
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
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<BankAccountInfoResponseDto> {

        val bankAccountSummaryResponseDto = bankAccountQueryService.getBankAccountSummary(bankAccountId, customUserDetails.memberId)
        val historyResponseDto = bankAccountQueryService.getHistory(bankAccountId, customUserDetails.memberId)

        return ResponseEntity.ok(BankAccountInfoResponseDto(bankAccountSummaryResponseDto, historyResponseDto))
    }

    @GetMapping("/balance/{bankAccountId}")
    fun getBalance(
        @PathVariable bankAccountId: Long,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<BalanceResponseDto> {

        return ResponseEntity.ok(bankAccountQueryService.getBalance(bankAccountId, customUserDetails.memberId))
    }

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
