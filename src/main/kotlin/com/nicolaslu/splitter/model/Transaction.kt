package com.nicolaslu.splitter.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "transactions")
data class Transaction (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "transaction_payer")
    @JsonIgnore
    var payer: User,

    @Column(name = "transaction_payer_email", nullable = false)
    var payerEmail: String,

    @ManyToOne
    @JoinColumn(name = "transaction_payee")
    @JsonIgnore
    var payee: User,

    @Column(name = "transaction_payee_email", nullable = false)
    var payeeEmail: String,

    @Column(name = "transaction_amount", nullable = false)
    var amount: Double,

    @Enumerated(EnumType.STRING)
    var status: TransactionStatus = TransactionStatus.ACTIVE,

    @OneToMany(mappedBy = "transaction")
    var transactionBalances: List<TransactionBalance>
)

enum class TransactionStatus {
    SETTLED,
    ACTIVE
}