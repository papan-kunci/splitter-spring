package com.nicolaslu.splitter.repository

import com.nicolaslu.splitter.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
    fun findByEmailContaining(keyword: String): List<User>
    fun findByFirstNameContaining(keyword: String): List<User>
    fun findByLastNameContaining(keyword: String): List<User>
}