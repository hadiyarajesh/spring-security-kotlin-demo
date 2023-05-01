package com.hadiyarajesh.spring_security_demo.controller

import com.hadiyarajesh.spring_security_demo.app.model.ApiResponse
import com.hadiyarajesh.spring_security_demo.app.utility.EndPoint
import com.hadiyarajesh.spring_security_demo.model.User
import com.hadiyarajesh.spring_security_demo.services.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EndPoint.ADMIN_ROOT_PATH)
class AdminController(
    private val adminService: AdminService
) {
    @GetMapping("/details")
    fun getAdminDetails(
        authentication: Authentication
    ): ResponseEntity<ApiResponse.Success<Map<String, User>>> {
        val email = authentication.principal as String
        val user = adminService.getAdminDetails(email = email)

        return ResponseEntity.ok(ApiResponse.Success(status = true, data = mapOf("user" to user)))
    }
}
