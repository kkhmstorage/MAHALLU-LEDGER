package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    INCOME,
    EXPENSE
}

@Entity(tableName = "cash_transactions")
data class CashTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mahalCode: String = "ALNOOR",
    val type: TransactionType,
    val category: String, // e.g. "Staff Honorarium", "Electricity & Water", "Friday Collection", "Donation", "Building Maintenance"
    val amount: Double,
    val transactionDate: Long = System.currentTimeMillis(),
    val title: String,
    val description: String = "",
    val voucherNumber: String = "",
    val paymentMode: String = "CASH" // CASH, UPI, BANK_TRANSFER, CHEQUE
)
