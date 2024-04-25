package com.nicolaslu.splitter.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nicolaslu.splitter.model.TransactionStatus

data class TransactionDto (
    val id: Int?,
    val payerEmail: String,
    val payeeEmail: String,
    val amount: Double,
    val status: TransactionStatus = TransactionStatus.ACTIVE,
    val transactionBalances: List<TransactionBalanceDto>
)