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
    val createdAt: Long = System.currentTimeMillis()
)
