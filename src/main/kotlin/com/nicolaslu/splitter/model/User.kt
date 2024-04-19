package com.nicolaslu.splitter.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nicolaslu.splitter.dto.user.UserInfo
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @Column(name = "email", nullable = false)
    var email: String,

    @Column(name = "first_name", nullable = false)
    var firstName: String = "",

    @Column(name = "last_name", nullable = false)
    var lastName: String = "",

    @OneToMany(mappedBy = "sender")
    var sentFriendRequests: List<FriendRequest> = listOf(),

    @OneToMany(mappedBy = "receiver")
    var receivedFriendRequests: List<FriendRequest> = listOf(),

    @OneToMany(mappedBy = "user")
    var friendships: List<Friendship> = listOf(),

    @OneToMany(mappedBy = "friend")
    val receivedFriendship: List<Friendship> = listOf()
) {
    //TODO: encrypt password
    @Column(name = "password")
    var password: String? = null

    @get:JsonIgnore
    val allFriendship: List<Friendship>
        get() = friendships + receivedFriendship

    @get:JsonIgnore
    val allFriends: List<User>
        get() = allFriendship.map { if (it.user == this) it.friend else it.user }

    val allFriendsEmail: List<String>
        get() = allFriends.map { it.email }

    fun isFriend(user: User): Boolean = allFriends.contains(user)
    fun toUserInfo(): UserInfo = UserInfo(
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName
    )
}