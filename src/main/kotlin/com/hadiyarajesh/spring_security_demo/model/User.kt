package com.hadiyarajesh.spring_security_demo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val email: String,
    @JsonIgnore
    val passWord: String,

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    val roles: List<Role> = emptyList()
) : UserDetails {
    @JsonIgnore
    override fun getAuthorities() = this.roles

    @JsonIgnore
    override fun getPassword() = this.passWord

    @JsonIgnore
    override fun getUsername() = this.email

    @JsonIgnore
    override fun isAccountNonExpired() = true

    @JsonIgnore
    override fun isAccountNonLocked() = true

    @JsonIgnore
    override fun isCredentialsNonExpired() = true

    @JsonIgnore
    override fun isEnabled() = true
}
