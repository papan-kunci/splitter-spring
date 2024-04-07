package com.nicolaslu.splitter.controller

import com.nicolaslu.splitter.model.User
import com.nicolaslu.splitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class AuthenticationController(@Autowired private val userRepository: UserRepository)  {
    @PostMapping
    fun login(@RequestBody user: User): ResponseEntity<User> {
        val user = userRepository.findByEmail(user.email)
        if (user != null) {
            return ResponseEntity(user, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}