package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "additional_collections")
data class AdditionalCollection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mahalCode: String = "ALNOOR",
    val title: String, // e.g. "റിലീഫ് ഫണ്ട് (Relief Fund)"
    val description: String = "",
    val targetAmountPerFamily: Double = 0.0, // Suggested per-family target
    val targetTotalFund: Double = 0.0, // Overall target for the fund
    val category: String = "Special Fund", // "Relief", "Madrasa Building", "Festival/Eid", "General"
    val dueDate: Long = 0L,
    val createdDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

@Entity(tableName = "additional_collection_payments")
data class AdditionalCollectionPayment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val collectionId: Long,
    val householdId: Long,
    val mahalCode: String = "ALNOOR",
    val householdName: String = "",
    val houseNumber: String = "",
    val amount: Double,
    val paymentDate: Long = System.currentTimeMillis(),
    val receiptNumber: String,
    val paymentMode: String = "CASH", // CASH, UPI, BANK_TRANSFER
    val collectorName: String = "Sub-Admin",
    val notes: String = ""
)

data class AdditionalCollectionSummary(
    val collection: AdditionalCollection,
    val totalCollected: Double,
    val contributorCount: Int,
    val payments: List<AdditionalCollectionPayment>
)
