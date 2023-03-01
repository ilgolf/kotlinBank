package me.golf.kotlin.global.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import me.golf.kotlin.global.jwt.dto.TokenBaseDto
import me.golf.kotlin.global.security.CustomUserDetailsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider(
    @param:Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.accessToken-validity-in-seconds}") private val accessTokenValidityInSeconds: Long,
    @Value("\${jwt.refreshToken-validity-in-seconds}") private val refreshTokenValidityInSeconds: Long,
    private val customUserDetailsService: CustomUserDetailsService
) : InitializingBean {

    private val log = LoggerFactory.getLogger(TokenProvider::class.java)
    private val accessTokenValidityInMilliSeconds: Long = accessTokenValidityInSeconds * 1000
    private val refreshTokenValidityInMilliSeconds: Long = refreshTokenValidityInSeconds * 1000
    private var key: Key? = null

    companion object {
        private const val AUTHORITIES_KEY = "Authorization"
    }

    override fun afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }

    fun createToken(memberId: Long, authentication: Authentication): TokenBaseDto {
        val authorities = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        val accessTokenExpiredTime = Date(Date().time + accessTokenValidityInMilliSeconds)
        val refreshTokenExpiredTime = Date(Date().time + refreshTokenValidityInMilliSeconds)

        val accessToken = Jwts.builder()
            .claim("memberId", memberId.toString())
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiredTime)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        val refreshToken = Jwts.builder()
            .claim("memberId", memberId.toString())
            .setExpiration(refreshTokenExpiredTime)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        return TokenBaseDto(accessToken = accessToken, refreshToken = refreshToken)
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val authorities = Arrays.stream(claims[AUTHORITIES_KEY]
            .toString()
            .split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())

        val memberId = claims["memberId"]

        val userDetails = customUserDetailsService.loadUserByUsername(memberId = memberId.toString())

        return UsernamePasswordAuthenticationToken(userDetails, "", authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            log.error("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            log.error("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            log.error("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            log.error("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            log.error("JWT 토큰이 잘못되었습니다.")
        }

        return false
    }
}