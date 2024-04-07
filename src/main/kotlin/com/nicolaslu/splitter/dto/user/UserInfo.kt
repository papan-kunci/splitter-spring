package com.nicolaslu.splitter.dto.user

data class UserInfo(
    var email: String,
    var firstName: String = "",
    var lastName: String = ""
) {
}