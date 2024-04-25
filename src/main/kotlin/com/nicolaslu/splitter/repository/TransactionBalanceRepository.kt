package com.nicolaslu.splitter.repository

import com.nicolaslu.splitter.model.TransactionBalance
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionBalanceRepository: JpaRepository<TransactionBalance, Int> {
}