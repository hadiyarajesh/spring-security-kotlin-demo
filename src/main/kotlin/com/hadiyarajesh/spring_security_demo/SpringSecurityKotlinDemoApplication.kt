package com.hadiyarajesh.spring_security_demo

import com.hadiyarajesh.spring_security_demo.model.Role
import com.hadiyarajesh.spring_security_demo.model.RoleName
import com.hadiyarajesh.spring_security_demo.model.User
import com.hadiyarajesh.spring_security_demo.services.RoleService
import com.hadiyarajesh.spring_security_demo.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder

@ConfigurationPropertiesScan
@SpringBootApplication
class SpringSecurityKotlinDemoApplication {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun init(
        roleService: RoleService,
        userService: UserService,
        passwordEncoder: PasswordEncoder
    ) = CommandLineRunner { args ->
        logger.info("Setting up the initial database...")
        setupInitialDatabase(
            roleService = roleService,
            userService = userService,
            passwordEncoder = passwordEncoder
        )
    }
}

fun main(args: Array<String>) {
    runApplication<SpringSecurityKotlinDemoApplication>(*args)
}

private fun setupInitialDatabase(
    roleService: RoleService,
    userService: UserService,
    passwordEncoder: PasswordEncoder
) {
    roleService.createRoleIfNotExists(role = Role(roleName = RoleName.USER))
    roleService.createRoleIfNotExists(role = Role(roleName = RoleName.ADMIN))

    val userRole = roleService.findRoleByName(RoleName.USER)
    val adminRole = roleService.findRoleByName(RoleName.ADMIN)

    if (userRole != null) {
        userService.createUserIfNotExists(
            User(
                name = "User",
                email = "user@test.com",
                passWord = passwordEncoder.encode("user-password"),
                roles = listOf(userRole)
            )
        )
    }

    if (userRole != null && adminRole != null) {
        userService.createUserIfNotExists(
            User(
                name = "Admin",
                email = "admin@test.com",
                passWord = passwordEncoder.encode("admin-password"),
                roles = listOf(userRole, adminRole)
            )
        )
    }
}
