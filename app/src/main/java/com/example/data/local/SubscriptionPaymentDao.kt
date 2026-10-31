package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SubscriptionPayment
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionPaymentDao {
    @Query("SELECT * FROM subscription_payments WHERE year = :year ORDER BY paymentDate DESC")
    fun getPaymentsForYear(year: Int): Flow<List<SubscriptionPayment>>

    @Query("SELECT * FROM subscription_payments WHERE householdId = :householdId AND year = :year ORDER BY month ASC")
    fun getPaymentsForHousehold(householdId: Long, year: Int): Flow<List<SubscriptionPayment>>

    @Query("SELECT * FROM subscription_payments WHERE householdId = :householdId AND year = :year ORDER BY month ASC")
    suspend fun getPaymentsForHouseholdSync(householdId: Long, year: Int): List<SubscriptionPayment>

    @Query("SELECT * FROM subscription_payments WHERE receiptNumber = :receiptNumber LIMIT 1")
    suspend fun getPaymentByReceipt(receiptNumber: String): SubscriptionPayment?

    @Query("SELECT SUM(amountPaid) FROM subscription_payments WHERE year = :year")
    fun getTotalSubscriptionCollected(year: Int): Flow<Double?>

    @Query("SELECT SUM(amountPaid) FROM subscription_payments WHERE year = :year AND month = :month")
    fun getMonthlySubscriptionCollected(year: Int, month: Int): Flow<Double?>

    @Query("SELECT * FROM subscription_payments ORDER BY paymentDate DESC LIMIT :limit")
    fun getRecentPayments(limit: Int): Flow<List<SubscriptionPayment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: SubscriptionPayment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<SubscriptionPayment>)

    @Delete
    suspend fun deletePayment(payment: SubscriptionPayment)
}
