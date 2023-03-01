package me.golf.kotlin.global.jwt

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtSecurityConfig(private val tokenProvider: TokenProvider)
    : SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        http
            .addFilterBefore(
                JwtFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }
}