package com.nicolaslu.splitter.repository

import com.nicolaslu.splitter.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Int>