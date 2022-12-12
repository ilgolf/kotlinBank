package me.golf.kotlin.domain.account.presentation

import me.golf.kotlin.domain.account.application.AccountService
import me.golf.kotlin.domain.account.dto.AccessTokenResponseDto
import me.golf.kotlin.domain.account.dto.LoginRequestDto
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/api/v2")
class AccountController(
    private val accountService: AccountService
) {

    @PostMapping("/public/account")
    fun login(@Valid @RequestBody requestDto: LoginRequestDto): ResponseEntity<AccessTokenResponseDto> {
        val resultToken = accountService.login(requestDto.email, requestDto.password)
        val responseDto = AccessTokenResponseDto(resultToken.accessToken, true)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, getCookie(resultToken.refreshToken).toString()).body(responseDto)
    }
    
    @DeleteMapping("/account")
    fun logout(response: HttpServletResponse): ResponseEntity<Unit> {
        val cookie = Cookie("Set-Cookie", null)
        cookie.maxAge = 0
        cookie.path = "/"
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/public/account/reissue")
    fun reissueToken(@CookieValue(name = "refreshToken") refreshToken: String) =
        ResponseEntity.ok(AccessTokenResponseDto(accountService.reissue(refreshToken), true))

    private fun getCookie(refreshToken: String): ResponseCookie {
        return ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(18000)
            .build()
    }
}