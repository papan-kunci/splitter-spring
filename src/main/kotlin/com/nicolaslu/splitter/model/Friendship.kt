package com.nicolaslu.splitter.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "friendship",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "friend_id"])]
)
data class Friendship (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User,

    @Column(name = "user_email", nullable = false)
    var userEmail: String,

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @JsonIgnore
    var friend: User,

    @Column(name = "friend_email", nullable = false)
    var friendEmail: String,

    @Enumerated(EnumType.STRING)
    var status: FriendshipStatus = FriendshipStatus.PENDING
)

enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}