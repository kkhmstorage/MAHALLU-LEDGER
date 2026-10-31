package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subscription_payments",
    foreignKeys = [
        ForeignKey(
            entity = Household::class,
            parentColumns = ["id"],
            childColumns = ["householdId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["householdId"]),
        Index(value = ["householdId", "year", "month"], unique = true)
    ]
)
data class SubscriptionPayment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val householdId: Long,
    val month: Int, // 1 = Jan, 12 = Dec
    val year: Int,
    val amountPaid: Double,
    val paymentDate: Long = System.currentTimeMillis(),
    val paymentMethod: String = "CASH", // CASH, UPI, BANK_TRANSFER
    val receiptNumber: String,
    val collectedBy: String = "Treasurer",
    val notes: String = ""
)
