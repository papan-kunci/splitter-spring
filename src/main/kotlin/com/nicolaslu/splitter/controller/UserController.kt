package com.nicolaslu.splitter.controller

import com.nicolaslu.splitter.dto.user.UserInfo
import com.nicolaslu.splitter.model.User
import com.nicolaslu.splitter.repository.UserRepository
import com.nicolaslu.splitter.util.from
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
import java.util.*
import kotlin.Exception

@RestController
@RequestMapping("/api/v1/user")
class UserController(@Autowired private val userRepository: UserRepository) {

    @GetMapping
    fun getAllUser(): List<User> = userRepository.findAll().toList()

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<UserInfo> {
        var normalizedUser = user.copy(email = user.email.lowercase())
        checkIfUserExists(email = normalizedUser.email, expectUserExists = false)
        //TODO: handle password properly
        normalizedUser.password = user.password
        val savedUser = userRepository.save(normalizedUser)
        println("User created: ${savedUser.email}")
        return ResponseEntity(UserInfo(savedUser.email).from(savedUser), HttpStatus.CREATED)
    }


    @GetMapping("/{email}")
    fun getUserByEmail(@PathVariable("email") userEmail: String): ResponseEntity<UserInfo> {
        val user = userRepository.findByEmail(userEmail)
        if (user != null) {
            return ResponseEntity(UserInfo(user.email).from(user), HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{email}")
    fun updateUserById(@PathVariable("email") userEmail: String, @RequestBody user: User): ResponseEntity<UserInfo> {
        val existingUser = userRepository.findByEmail(userEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val updatedUser = existingUser.copy(firstName = user.firstName, lastName = user.lastName, email = user.email.lowercase())
        userRepository.save(updatedUser)
        println("User data updated: ${updatedUser.email}")
        return ResponseEntity(UserInfo(updatedUser.email).from(updatedUser), HttpStatus.OK)
    }

    @DeleteMapping
    fun deleteUserById(@RequestBody user: User): ResponseEntity<Boolean> {
        val user = userRepository.findByEmail(user.email.lowercase()) ?: return ResponseEntity(false, HttpStatus.NOT_FOUND)
        println("User deleted: ${user.email}")
        userRepository.deleteById(user.id)
        return ResponseEntity(true, HttpStatus.NO_CONTENT)
    }

    fun checkIfUserExists(email: String, expectUserExists: Boolean) {
        val user = userRepository.findByEmail(email.lowercase())
        if (user != null && !expectUserExists) {
            throw Exception("Email has already been registered")
        }
        if (user == null && expectUserExists) {
            throw Exception("User not exist")
        }
    }
}