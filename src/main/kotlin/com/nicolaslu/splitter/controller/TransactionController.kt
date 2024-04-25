package com.nicolaslu.splitter.controller

import com.nicolaslu.splitter.dto.TransactionDto
import com.nicolaslu.splitter.model.Transaction
import com.nicolaslu.splitter.model.TransactionBalance
import com.nicolaslu.splitter.repository.TransactionRepository
import com.nicolaslu.splitter.repository.TransactionBalanceRepository
import com.nicolaslu.splitter.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/transaction")
class TransactionController(
    @Autowired private val transactionRepository: TransactionRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val transactionBalanceRepository: TransactionBalanceRepository
) {

    @GetMapping
    fun getAllTransactions(): List<Transaction> = transactionRepository.findAll().toList()

    @DeleteMapping
    fun deleteAllTransactions() {
        transactionRepository.deleteAll()
    }

    @GetMapping("/transaction-balance")
    fun getAllTransactionValues(): List<TransactionBalance> = transactionBalanceRepository.findAll().toList()

    @DeleteMapping("/transaction-balance")
    fun deleteAllTransactionValues() {
        transactionBalanceRepository.deleteAll()
    }

    @PostMapping("/individual")
    fun createIndividualTransaction(@RequestBody transaction: TransactionDto): ResponseEntity<Transaction> {
        val payer = userRepository.findByEmail(transaction.payerEmail)
        val payee = userRepository.findByEmail(transaction.payeeEmail)

        if (payer == null || payee == null) { return ResponseEntity(HttpStatus.NOT_FOUND) }
        val existingTransaction = transactionRepository.findByPayeeAndPayer(payee = payee, payer = payer)
        val ts = if (existingTransaction == null ) {
            Transaction(
                payee = payee,
                payeeEmail = transaction.payeeEmail,
                payer = payer,
                payerEmail = transaction.payerEmail,
                amount = transaction.amount,
                status = transaction.status,
                transactionBalances = listOf()
            )
        } else {
            existingTransaction.copy(
                id = existingTransaction.id,
                payee = payee,
                payeeEmail = payee.email,
                payer = payer,
                payerEmail = payer.email,
                amount = transaction.amount,
                status = transaction.status
            )
        }
        transactionRepository.save(ts)
        //TODO: check both transaction balance user exist
        transaction.transactionBalances.mapNotNull {
            val usr = userRepository.findByEmail(it.userEmail)
            if (usr != null) {
                val tv = transactionBalanceRepository.save(TransactionBalance(userEmail = it.userEmail, value = it.value, transaction = ts))
                tv
            } else {
                null
            }
        }
        println("Transaction created")
        return ResponseEntity(HttpStatus.OK)
    }

    @PutMapping("/individual")
    fun updateIndividualTransaction(@RequestBody transaction: TransactionDto): ResponseEntity<Transaction> {
        val payer = userRepository.findByEmail(transaction.payerEmail)
        val payee = userRepository.findByEmail(transaction.payeeEmail)
        if (payer == null || payee == null) { return ResponseEntity(HttpStatus.NOT_FOUND) }
        val existingTransaction = transactionRepository.findByPayeeAndPayer(payee = payee, payer = payer) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        existingTransaction.transactionBalances.map {transactionBalance ->
            if (transactionBalance.id != null) {
                val existingBalance = transactionBalanceRepository.findById(transactionBalance.id!!)
                existingBalance.ifPresent {
                    transactionBalanceRepository.delete(it)
                }
            }
        }

        val updatedTransaction = existingTransaction.copy(
            payee = payee,
            payeeEmail = transaction.payeeEmail,
            payer = payer,
            payerEmail = transaction.payerEmail,
            amount = transaction.amount,
            status = transaction.status,
            transactionBalances = listOf()
        )

        transactionRepository.save(updatedTransaction)

        transaction.transactionBalances.mapNotNull {
            val usr = userRepository.findByEmail(it.userEmail)
            if (usr != null) {
                val tv = transactionBalanceRepository.save(TransactionBalance(userEmail = it.userEmail, value = it.value, transaction = existingTransaction))
                tv
            } else {
                null
            }
        }
        println("Transaction updated")
        return ResponseEntity(HttpStatus.OK)
    }
}