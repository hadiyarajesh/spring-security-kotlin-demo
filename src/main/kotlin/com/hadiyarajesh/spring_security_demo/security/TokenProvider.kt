package com.hadiyarajesh.spring_security_demo.security

import com.hadiyarajesh.spring_security_demo.app.exception.InvalidJwtException
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.lang.Exception
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.function.Function

@Component
class TokenProvider(
    private val securityProperties: SecurityProperties
) {
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(securityProperties.secret).parseClaimsJws(token).body
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun extractUsername(token: String): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    private fun generateTokenFromClaims(
        claims: Claims,
        tokenValidity: Long
    ): String {
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuer("SpringSecurityKotlinDemoApplication")
            .setIssuedAt(now)
            .setExpiration(Date(now.time + tokenValidity))
            .signWith(SignatureAlgorithm.HS256, securityProperties.secret)
            .compact()
    }

    fun generateToken(
        userId: Long,
        email: String,
        roles: List<String>
    ): String {
        val claims = Jwts.claims().setSubject(email).apply {
            this["role"] = roles
        }
        return generateTokenFromClaims(claims, securityProperties.expiration)
    }

    fun generateRefreshToken(claims: Claims): String {
        return generateTokenFromClaims(claims, securityProperties.expiration)
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(securityProperties.secret).parseClaimsJws(token)
            return true
        } catch (e: MalformedJwtException) {
            throw InvalidJwtException("JWT token is malformed.")
        } catch (e: ExpiredJwtException) {
            throw InvalidJwtException("JWT token is expired.")
        } catch (e: Exception) {
            throw InvalidJwtException("JWT token validation failed.")
        }
    }

    fun getTokenFromHeader(httpServletRequest: HttpServletRequest): String? {
        val bearerToken = httpServletRequest.getHeader("Authorization")
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else {
            null
        }
    }

    fun getClaimsFromToken(token: String): Jws<Claims>? {
        return Jwts.parser()
            .setSigningKey(securityProperties.secret)
            .parseClaimsJws(token)
    }
}
