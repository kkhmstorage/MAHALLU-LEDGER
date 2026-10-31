package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "households")
data class Household(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mahalCode: String = "ALNOOR",
    val houseNumber: String,
    val headOfFamily: String,
    val familyName: String = "",
    val phoneNumber: String,
    val monthlyFee: Double = 200.0,
    val wardNumber: String = "Ward 1",
    val memberCount: Int = 4,
    val maleCount: Int = 0,
    val femaleCount: Int = 0,
    val childrenCount: Int = 0,
    val rationCategory: String = "", // APL, BPL, AAY, etc.
    val madrasaStudentCount: Int = 0,
    val bloodGroups: String = "",
    val address: String = "",
    val notes: String = "",
    val verificationStatus: String = "VERIFIED", // "VERIFIED", "PENDING_MEMBER_INFO", "PENDING_VERIFICATION"
    val verifiedBy: String = "",
    val verifiedDate: Long? = null,
    val isActive: Boolean = true
)

data class MonthPaymentStatus(
    val month: Int, // 1..12
    val monthName: String,
    val isPaid: Boolean,
    val amountPaid: Double = 0.0,
    val paymentDate: Long? = null,
    val receiptNumber: String? = null
)

data class HouseholdSubscriptionSummary(
    val household: Household,
    val paidMonthsCount: Int,
    val pendingMonthsCount: Int,
    val totalPaid: Double,
    val totalPendingDue: Double,
    val monthsStatus: List<MonthPaymentStatus>
)
