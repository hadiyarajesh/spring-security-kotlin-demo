package com.hadiyarajesh.spring_security_demo.repositories

import com.hadiyarajesh.spring_security_demo.model.Role
import com.hadiyarajesh.spring_security_demo.model.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun existsByRoleName(roleName: RoleName): Boolean

    fun findByRoleName(roleName: RoleName): Role?
}
