package com.hadiyarajesh.spring_security_demo.services

import com.hadiyarajesh.spring_security_demo.app.exception.ResourceAlreadyExistException
import com.hadiyarajesh.spring_security_demo.app.exception.ResourceNotFoundException
import com.hadiyarajesh.spring_security_demo.model.RoleName
import com.hadiyarajesh.spring_security_demo.model.User
import com.hadiyarajesh.spring_security_demo.model.UserAndToken
import com.hadiyarajesh.spring_security_demo.model.dto.SignInDto
import com.hadiyarajesh.spring_security_demo.model.dto.SignUpDto
import com.hadiyarajesh.spring_security_demo.repositories.RoleRepository
import com.hadiyarajesh.spring_security_demo.repositories.UserRepository
import com.hadiyarajesh.spring_security_demo.security.TokenProvider
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) {
    fun signUp(signUpDto: SignUpDto): UserAndToken {
        if (userRepository.existsByEmail(signUpDto.email)) {
            throw ResourceAlreadyExistException("Email ${signUpDto.email} is already in use, please use different email.")
        }

        val role = roleRepository.findByRoleName(RoleName.USER)!!

        val user = userRepository.save(
            User(
                name = signUpDto.fullName,
                email = signUpDto.email,
                passWord = passwordEncoder.encode(signUpDto.password),
                roles = listOf(role)
            )
        )

        val token = getToken(
            userId = user.id!!,
            username = user.email,
            roles = user.roles.map { it.roleName.name }
        )

        return UserAndToken(user = user, token = token)
    }

    fun signIn(signInDto: SignInDto): UserAndToken {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(signInDto.email, signInDto.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val user = userRepository.findByEmail(authentication.name)
            ?: throw ResourceNotFoundException("User with email ${signInDto.email} not found")

        val token = getToken(
            userId = user.id!!,
            username = user.email,
            roles = user.roles.map { it.roleName.name }
        )

        return UserAndToken(user = user, token = token)
    }


    fun getToken(userId: Long, username: String, roles: List<String> = emptyList()): String {
        return tokenProvider.generateToken(email = username, roles = roles)
    }

    fun getRefreshToken(claims: Claims): String {
        return tokenProvider.generateRefreshToken(claims)
    }
}
