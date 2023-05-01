package com.hadiyarajesh.spring_security_demo.services

import com.hadiyarajesh.spring_security_demo.app.exception.ResourceNotFoundException
import com.hadiyarajesh.spring_security_demo.model.User
import com.hadiyarajesh.spring_security_demo.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val userRepository: UserRepository
) {
    fun getAdminDetails(email: String): User {
        return userRepository.findByEmail(email = email)
            ?: throw ResourceNotFoundException("Admin with email $email not found")
    }
}
