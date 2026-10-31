package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_profile")
data class CommunityProfile(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Al-Noor Madrasa & Mahal Committee",
    val registrationNumber: String = "REG-KL/2014/0821",
    val location: String = "Kerala, India",
    val phone: String = "+91 98460 12345",
    val email: String = "alnoor.mahal@example.com",
    val currencySymbol: String = "₹",
    val defaultMonthlyFee: Double = 200.0,
    val adminPin: String = "1234",
    val currentFinancialYear: Int = 2026
)
