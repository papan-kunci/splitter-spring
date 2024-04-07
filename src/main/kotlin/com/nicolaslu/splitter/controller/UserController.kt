package com.nicolaslu.splitter.controller

import com.nicolaslu.splitter.model.User
import com.nicolaslu.splitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(@Autowired private val userRepository: UserRepository) {

    @GetMapping
    fun getAllUser(): List<User> = userRepository.findAll().toList()

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val savedUser = userRepository.save(user)
        return ResponseEntity(savedUser, HttpStatus.CREATED)
    }


    @GetMapping("/{email}")
    fun getUserByEmail(@PathVariable("email") userEmail: String): ResponseEntity<User> {
        val user = userRepository.findByEmail(userEmail)
        if (user != null) {
            return ResponseEntity(user, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable("id") userId: Int, @RequestBody user: User): ResponseEntity<User> {
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val updatedUser = existingUser.copy(name = user.name, email = user.email)
        userRepository.save(updatedUser)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable("id") userId: Int): ResponseEntity<Boolean> {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        userRepository.deleteById(userId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}