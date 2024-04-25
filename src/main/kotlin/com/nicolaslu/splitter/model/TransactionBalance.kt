package com.nicolaslu.splitter.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "transaction_balances")
data class TransactionBalance (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "user_email", nullable = true)
    var userEmail: String,

    @Column(name = "value", nullable = false)
    var value: Double,

    @ManyToOne
    @JoinColumn(name = "transaction_ref")
    @JsonIgnore
    var transaction: Transaction
)