package com.nicolaslu.splitter.controller

import com.nicolaslu.splitter.model.FriendRequest
import com.nicolaslu.splitter.model.Friendship
import com.nicolaslu.splitter.model.User
import com.nicolaslu.splitter.repository.FriendshipRepository
import com.nicolaslu.splitter.repository.UserRepository
import com.nicolaslu.splitter.service.FriendRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
@RequestMapping("/api/v1/friends")
class FriendRequestController(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val friendshipRepository: FriendshipRepository,
    @Autowired private val friendRequestService: FriendRequestService
) {
    @PostMapping("/send-request/{senderEmail}/{receiverEmail}")
    fun sendFriendRequest(
        @PathVariable("senderEmail") senderEmail: String,
        @PathVariable("receiverEmail") receiverEmail: String
    ): ResponseEntity<FriendRequest> {
        try {
            val sender: User = userRepository.findByEmail(senderEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
            val receiver: User =
                userRepository.findByEmail(receiverEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
            friendRequestService.sendFriendRequest(sender, receiver)
            return ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/accept-request/{senderEmail}/{receiverEmail}")
    fun acceptFriendRequest(
        @PathVariable("senderEmail") senderEmail: String,
        @PathVariable("receiverEmail") receiverEmail: String
    ): ResponseEntity<FriendRequest> {
        try {
            val sender: User = userRepository.findByEmail(senderEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
            val receiver: User =
                userRepository.findByEmail(receiverEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
            friendRequestService.acceptFriendRequest(sender, receiver)
            return ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/delete-request/{senderEmail}/{receiverEmail}")
    fun deleteFriendRequest(
        @PathVariable("senderEmail") senderEmail: String,
        @PathVariable("receiverEmail") receiverEmail: String
    ): ResponseEntity<Void> {
        try {
            val sender: User = userRepository.findByEmail(senderEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
            val receiver: User =
                userRepository.findByEmail(receiverEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
            friendRequestService.deleteFriendRequest(sender, receiver)
            return ResponseEntity(HttpStatus.OK)
        } catch (ex: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/delete-friendship/{senderEmail}/{receiverEmail}")
    fun deleteFriendship(
        @PathVariable("senderEmail") senderEmail: String,
        @PathVariable("receiverEmail") receiverEmail: String
    ): ResponseEntity<Void> {
        val sender: User = userRepository.findByEmail(senderEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val receiver: User = userRepository.findByEmail(receiverEmail) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val friendship: Friendship? = friendshipRepository.findByUserAndFriend(sender, receiver)
        val friendshipReversed: Friendship? = friendshipRepository.findByUserAndFriend(receiver, sender)
        if (friendship != null) {
            friendshipRepository.delete(friendship)
            return ResponseEntity(HttpStatus.OK)
        }
        if (friendshipReversed != null) {
            friendshipRepository.delete(friendshipReversed)
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}