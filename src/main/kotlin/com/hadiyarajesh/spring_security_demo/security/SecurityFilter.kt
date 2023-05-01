package com.hadiyarajesh.spring_security_demo.security

import com.hadiyarajesh.spring_security_demo.app.utility.Constant
import com.hadiyarajesh.spring_security_demo.app.utility.EndPoint
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter(
    private val tokenProvider: TokenProvider,
    private val myUserDetailsService: MyUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = tokenProvider.getTokenFromHeader(request)

        if (token != null && tokenProvider.validateToken(token)) {
            val email = tokenProvider.extractEmail(token)
            val userDetails = myUserDetailsService.loadUserByUsername(email)
            val authentication =
                UsernamePasswordAuthenticationToken(userDetails.username, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }

        // Set request attribute for refresh toke request
        if (request.requestURI.equals(EndPoint.REFRESH_TOKEN)) {
            request.getAttribute(Constant.CLAIMS)
                ?: request.setAttribute(
                    Constant.CLAIMS,
                    tokenProvider.getClaimsFromToken(token!!)
                )
        }

        filterChain.doFilter(request, response)
    }
}
