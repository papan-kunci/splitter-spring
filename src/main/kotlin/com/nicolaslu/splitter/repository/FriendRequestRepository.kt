package com.nicolaslu.splitter.repository

import com.nicolaslu.splitter.model.FriendRequest
import com.nicolaslu.splitter.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface FriendRequestRepository: JpaRepository<FriendRequest, Int> {
    fun findBySender(sender: User): List<FriendRequest>
    fun findByReceiver(receiver: User): List<FriendRequest>
    fun findBySenderAndReceiver(sender: User, receiver: User): FriendRequest?
}