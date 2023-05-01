package com.hadiyarajesh.spring_security_demo.controller

import com.hadiyarajesh.spring_security_demo.app.model.ApiResponse
import com.hadiyarajesh.spring_security_demo.app.utility.EndPoint
import com.hadiyarajesh.spring_security_demo.model.User
import com.hadiyarajesh.spring_security_demo.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EndPoint.USER_ROOT_PATH)
class UserController(
    private val userService: UserService
) {
    @GetMapping("")
    fun getUsers(): ResponseEntity<ApiResponse.Success<Map<String, List<User>>>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(ApiResponse.Success(status = true, data = mapOf("users" to users)))
    }

    @PostMapping("/upgrade")
    fun upgradeToAdmin(
        authentication: Authentication
    ): ResponseEntity<ApiResponse.Success<Map<String, User>>> {
        val email = authentication.principal as String
        val user = userService.upgradeToAdmin(email = email)
        return ResponseEntity.ok(ApiResponse.Success(status = true, data = mapOf("user" to user)))
    }
}
