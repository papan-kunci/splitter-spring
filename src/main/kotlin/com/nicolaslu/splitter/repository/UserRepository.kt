package com.nicolaslu.splitter.repository

import com.nicolaslu.splitter.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}