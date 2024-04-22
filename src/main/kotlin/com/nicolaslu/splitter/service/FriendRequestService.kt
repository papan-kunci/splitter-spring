package com.nicolaslu.splitter.service

import com.nicolaslu.splitter.model.FriendRequest
import com.nicolaslu.splitter.model.Friendship
import com.nicolaslu.splitter.model.FriendshipStatus
import com.nicolaslu.splitter.model.User
import com.nicolaslu.splitter.repository.FriendRequestRepository
import com.nicolaslu.splitter.repository.FriendshipRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Transactional
class FriendRequestService(
    @Autowired private val friendshipRepository: FriendshipRepository,
    @Autowired private val friendRequestRepository: FriendRequestRepository
) {
    fun sendFriendRequest(sender: User, receiver: User) {
        val existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
        val existingFriendship = friendshipRepository.findByUserAndFriend(sender,receiver)
        val existingFriendshipReversed = friendshipRepository.findByUserAndFriend(receiver,sender)
        if (existingRequest != null || existingFriendship != null || existingFriendshipReversed != null) {
            throw Exception("Friend request or friendship already exists")
        }
        val friendRequest = FriendRequest(sender = sender, senderEmail = sender.email, receiver = receiver, receiverEmail = receiver.email)
        friendRequestRepository.save(friendRequest)
    }

    fun acceptFriendRequest(sender: User, receiver: User) {
        val existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver) ?: throw Exception("Friend request not exist")
        val friendship = Friendship(user = sender, userEmail = sender.email, friend = receiver, friendEmail = receiver.email, status = FriendshipStatus.ACCEPTED)
        friendshipRepository.save(friendship)
        friendRequestRepository.delete(existingRequest)
    }

    fun deleteFriendRequest(sender: User, receiver: User) {
        val existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
            ?: throw Exception("Friend request not exist")
        friendRequestRepository.delete(existingRequest)
    }
}