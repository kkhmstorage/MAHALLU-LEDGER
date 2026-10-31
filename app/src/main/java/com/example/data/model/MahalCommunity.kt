package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mahal_communities")
data class MahalCommunity(
    @PrimaryKey
    val mahalCode: String, // e.g. "ALNOOR", "MHL-102"
    val name: String,
    val location: String,
    val subAdminUsername: String,
    val subAdminPassword: String,
    val contactPhone: String,
    val regNumber: String = "",
    val defaultMonthlyFee: Double = 200.0,
    val isActive: Boolean = true,
    val logoUri: String = "",
    val logoPreset: String = "MOSQUE",
    val upiId: String = "alnoor@upi",
    val payeeName: String = "Al-Noor Mahal Committee",
    val bankName: String = "State Bank of India",
    val accountNumber: String = "67329104821",
    val ifscCode: String = "SBIN0008421",
    val qrCodeUri: String = "",
    val subscriptionPlan: String = "PREMIUM",
    val subscriptionStatus: String = "ACTIVE",
    val subscriptionExpiryDate: Long = System.currentTimeMillis() + (365L * 24 * 3600 * 1000), // 1 year
    val createdAt: Long = System.currentTimeMillis()
)
