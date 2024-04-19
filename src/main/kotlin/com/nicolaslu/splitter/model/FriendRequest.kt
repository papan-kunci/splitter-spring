package com.nicolaslu.splitter.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "friend_requests",
    uniqueConstraints = [UniqueConstraint(columnNames = ["sender_id", "receiver_id"])]
)
data class FriendRequest (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    var sender: User,

    @Column(name = "sender_email", nullable = false)
    var senderEmail: String,

    @ManyToOne
    @JoinColumn(name= "receiver_id")
    @JsonIgnore
    var receiver: User,

    @Column(name = "receiver_email", nullable = false)
    var receiverEmail: String,

    @Enumerated(EnumType.STRING)
    var status: RequestStatus = RequestStatus.PENDING
)

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}