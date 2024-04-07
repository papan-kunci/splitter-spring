package com.nicolaslu.splitter.util

import com.nicolaslu.splitter.dto.user.UserInfo
import com.nicolaslu.splitter.model.User
import org.springframework.stereotype.Component

@Component
class UserUtils {
}

fun UserInfo.from(user: User): UserInfo {
    this.email = user.email
    this.firstName = user.firstName
    this.lastName = user.lastName
    return this
}