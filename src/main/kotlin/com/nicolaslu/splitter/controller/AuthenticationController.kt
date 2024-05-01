package com.nicolaslu.splitter.controller

import com.nicolaslu.splitter.model.User
import com.nicolaslu.splitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class AuthenticationController(
    @Autowired private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
)  {
    @PostMapping
    fun login(@RequestBody user: User): ResponseEntity<User> {
        var normalizedUser = user.copy(email = user.email.lowercase())
        val existingUser = userRepository.findByEmail(normalizedUser.email)
        if (existingUser != null) {
            val isMatch = passwordEncoder.matches(user.password, existingUser.password)
            if (!isMatch) {
                println("Login failed: Wrong password")
                return ResponseEntity(HttpStatus.UNAUTHORIZED)
            }
            println("Login successful: ${existingUser.email}")
            return ResponseEntity(existingUser, HttpStatus.OK)
        }
        println("Login failed: User not found")
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}