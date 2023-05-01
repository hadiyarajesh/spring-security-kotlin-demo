package com.hadiyarajesh.spring_security_demo.controller

import com.hadiyarajesh.spring_security_demo.app.model.ApiResponse
import com.hadiyarajesh.spring_security_demo.app.utility.Constant
import com.hadiyarajesh.spring_security_demo.app.utility.EndPoint
import com.hadiyarajesh.spring_security_demo.model.UserAndToken
import com.hadiyarajesh.spring_security_demo.model.dto.SignInDto
import com.hadiyarajesh.spring_security_demo.model.dto.SignUpDto
import com.hadiyarajesh.spring_security_demo.services.AuthService
import io.jsonwebtoken.Claims
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EndPoint.AUTH_ROOT_PATH)
class AuthController(
    private val authService: AuthService
) {
    @PostMapping(EndPoint.SIGN_UP)
    fun signUp(@RequestBody signUpDto: SignUpDto): ResponseEntity<ApiResponse.Success<UserAndToken>> {
        val userAndToken = authService.signUp(signUpDto)
        return ResponseEntity.ok(ApiResponse.Success(true, userAndToken))
    }

    @PostMapping(EndPoint.SIGN_IN)
    fun signIn(@RequestBody signInDto: SignInDto): ResponseEntity<ApiResponse.Success<UserAndToken>> {
        val userAndToken = authService.signIn(signInDto)
        return ResponseEntity.ok(ApiResponse.Success(true, userAndToken))
    }

    @GetMapping(EndPoint.REFRESH_TOKEN)
    fun refreshToken(request: HttpServletRequest): ResponseEntity<ApiResponse.Success<Map<String, String>>> {
        val claims = request.getAttribute(Constant.CLAIMS) as Claims

        val token = authService.getRefreshToken(claims = claims)

        return ResponseEntity.ok(ApiResponse.Success(true, mapOf("token" to token)))
    }
}
