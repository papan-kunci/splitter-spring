package com.nicolaslu.splitter.controller

import com.nicolaslu.splitter.dto.user.UserInfo
import com.nicolaslu.splitter.model.User
import com.nicolaslu.splitter.repository.UserRepository
import com.nicolaslu.splitter.util.from
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    @Autowired private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @GetMapping
    fun getAllUser(): List<User> = userRepository.findAll().toList()

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<UserInfo> {
        var normalizedUser = user.copy(email = user.email.lowercase())
        checkIfUserExists(email = normalizedUser.email, expectUserExists = false)
        val encodedPassword = passwordEncoder.encode(user.password)
        normalizedUser.password = encodedPassword
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

    @GetMapping("/containing/{keyword}")
    fun getUsersByKeyword(@PathVariable("keyword") keyword: String): ResponseEntity<List<User>> {
        val keyword = keyword.lowercase()
        val userByEmail = userRepository.findByEmail(keyword)
        val usersByFirstName = userRepository.findByFirstNameContaining(keyword)
        val usersByLastName = userRepository.findByLastNameContaining(keyword)

        val combinedUsersSet = mutableSetOf<User>()
        if (userByEmail != null) { combinedUsersSet.add(userByEmail) }
        combinedUsersSet.addAll(usersByFirstName)
        combinedUsersSet.addAll(usersByLastName)

        return if (combinedUsersSet.toList().isEmpty()) {
            ResponseEntity(listOf(), HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(combinedUsersSet.toList(), HttpStatus.OK)
        }
    }

    @PutMapping("/{email}")
    fun updateUserById(@PathVariable("email") userEmail: String, @RequestBody user: User): ResponseEntity<UserInfo> {
        val existingUser = userRepository.findByEmail(userEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        if (user.firstName != null && user.firstName != "") {
            println(user.firstName)
            existingUser.firstName = user.firstName
        }
        if (user.lastName != null && user.lastName != "") {
            println(user.lastName)
            existingUser.lastName = user.lastName
        }
        userRepository.save(existingUser)
        println("User data updated: ${existingUser.email}")
        return ResponseEntity(UserInfo(existingUser.email).from(existingUser), HttpStatus.OK)
    }

    @PutMapping("/update-password/{email}")
    fun updateUserPassword(@PathVariable("email") userEmail: String, @RequestBody user: User): ResponseEntity<UserInfo> {
        val existingUser = userRepository.findByEmail(userEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val encodedPassword = passwordEncoder.encode(user.password)
        existingUser.password = encodedPassword
        userRepository.save(existingUser)
        return ResponseEntity(existingUser.toUserInfo(), HttpStatus.OK)
    }

    @DeleteMapping
    fun deleteUserByEmail(@RequestBody user: User): ResponseEntity<Boolean> {
        val existingUser = userRepository.findByEmail(user.email.lowercase()) ?: return ResponseEntity(false, HttpStatus.NOT_FOUND)
        val passwordMatch = passwordEncoder.matches(user.password, existingUser.password)
        if (!passwordMatch) {
            return ResponseEntity(false, HttpStatus.UNAUTHORIZED)
        }
        println("User deleted: ${existingUser.email}")
        userRepository.deleteById(existingUser.id)
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