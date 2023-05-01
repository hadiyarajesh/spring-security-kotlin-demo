package com.hadiyarajesh.spring_security_demo.services

import com.hadiyarajesh.spring_security_demo.model.Role
import com.hadiyarajesh.spring_security_demo.model.RoleName
import com.hadiyarajesh.spring_security_demo.repositories.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {
    fun createRoleIfNotExists(role: Role): Role {
        return if (roleRepository.existsByRoleName(role.roleName)) {
            roleRepository.findByRoleName(role.roleName)!!
        } else {
            roleRepository.save(role)
        }
    }

    fun findRoleByName(roleName: RoleName): Role? {
        return roleRepository.findByRoleName(roleName)
    }
}
