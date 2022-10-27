package me.golf.kotlin.global.config

import me.golf.kotlin.global.jwt.JwtSecurityConfig
import me.golf.kotlin.global.jwt.TokenProvider
import me.golf.kotlin.global.jwt.error.JwtAccessDeniedHandler
import me.golf.kotlin.global.jwt.error.JwtAuthenticationEntryPoint
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val corsFilter: CorsFilter,
    private val tokenProvider: TokenProvider,
    private val authenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val accessDeniedHandler: JwtAccessDeniedHandler
) {

    companion object {
        private const val PUBLIC_POINT = "/public/**"
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().antMatchers("/favicon.ico", "/", "/h2-console/**", "/error/**")
        }
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()

            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java)

            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
            .antMatchers(PUBLIC_POINT).permitAll()
            .anyRequest().authenticated()
            .and()
            .userDetailsService(customUserDetailsService)
            .apply(JwtSecurityConfig(tokenProvider))

        return http.build()
    }
}