package me.golf.kotlin.global.jwt

import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtFilter(private val tokenProvider: TokenProvider) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val jwt = resolveToken(httpServletRequest)?: ""
        val requestURI = httpServletRequest.requestURI

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            val authentication = tokenProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
            Companion.logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.name, requestURI)
        } else {
            Companion.logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI)
        }

        chain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)

        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtFilter::class.java)
        const val AUTHORIZATION_HEADER = "Authorization"
    }
}
