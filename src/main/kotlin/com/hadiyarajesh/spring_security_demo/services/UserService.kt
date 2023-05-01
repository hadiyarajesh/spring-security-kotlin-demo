package com.hadiyarajesh.spring_security_demo.services

import com.hadiyarajesh.spring_security_demo.app.exception.ResourceNotFoundException
import com.hadiyarajesh.spring_security_demo.model.RoleName
import com.hadiyarajesh.spring_security_demo.model.User
import com.hadiyarajesh.spring_security_demo.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleService: RoleService
) {
    fun createUserIfNotExists(user: User): User {
        return if (userRepository.existsByEmail(user.email)) {
            userRepository.findByEmail(user.email)!!
        } else {
            userRepository.save(user)
        }
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun upgradeToAdmin(email: String): User {
        val user = userRepository.findByEmail(email) ?: throw ResourceNotFoundException("User $email not found")

        val adminRole =
            roleService.findRoleByName(RoleName.ADMIN) ?: throw ResourceNotFoundException("Admin role not found")

        val roles = user.roles.toMutableList()
        roles.add(adminRole)

        return userRepository.save(user.copy(roles = roles))
    }
}
