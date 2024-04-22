package com.nicolaslu.splitter.repository

import com.nicolaslu.splitter.model.Friendship
import com.nicolaslu.splitter.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface FriendshipRepository: JpaRepository<Friendship, Int> {
    fun findByUserOrFriend(user: User, friend: User): List<Friendship>
    fun findByUserAndFriend(user:User, friend: User): Friendship?
}