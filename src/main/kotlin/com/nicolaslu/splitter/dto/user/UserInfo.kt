package com.nicolaslu.splitter.dto.user

import com.nicolaslu.splitter.model.FriendRequest
import com.nicolaslu.splitter.model.Friendship
import com.nicolaslu.splitter.model.User
import jakarta.persistence.OneToMany

data class UserInfo(
    var email: String,
    var firstName: String = "",
    var lastName: String = "",
    var sentFriendRequests: List<FriendRequest> = listOf(),
    var receivedFriendRequests: List<FriendRequest> = listOf(),
    var friendships: List<Friendship> = listOf(),
    var receivedFriendships: List<Friendship> = listOf(),
    var allFriendsEmail: List<String> = listOf()
    ) {
    var token: String? = null
}