package com.nicolaslu.splitter.repository

import com.nicolaslu.splitter.model.Transaction
import com.nicolaslu.splitter.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository: JpaRepository<Transaction, Int> {
    fun findByPayeeAndPayer(payee: User, payer: User): Transaction?
}