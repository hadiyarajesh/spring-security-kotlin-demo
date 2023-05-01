package com.hadiyarajesh.spring_security_demo.security

import com.hadiyarajesh.spring_security_demo.app.exception.ResourceNotFoundException
import com.hadiyarajesh.spring_security_demo.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class MyUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findByEmail(email) ?: throw ResourceNotFoundException("User with email $email not found")
    }
}
