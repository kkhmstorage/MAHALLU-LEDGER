package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CashTransaction
import com.example.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CashTransactionDao {
    @Query("SELECT * FROM cash_transactions ORDER BY transactionDate DESC")
    fun getAllTransactions(): Flow<List<CashTransaction>>

    @Query("SELECT * FROM cash_transactions WHERE mahalCode = :mahalCode ORDER BY transactionDate DESC")
    fun getTransactionsByMahal(mahalCode: String): Flow<List<CashTransaction>>

    @Query("SELECT * FROM cash_transactions WHERE type = :type ORDER BY transactionDate DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<CashTransaction>>

    @Query("SELECT * FROM cash_transactions WHERE transactionDate >= :startDate AND transactionDate <= :endDate ORDER BY transactionDate DESC")
    fun getTransactionsBetween(startDate: Long, endDate: Long): Flow<List<CashTransaction>>

    @Query("SELECT SUM(amount) FROM cash_transactions WHERE type = 'INCOME'")
    fun getTotalGeneralIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM cash_transactions WHERE type = 'EXPENSE'")
    fun getTotalExpenses(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM cash_transactions WHERE mahalCode = :mahalCode AND type = 'INCOME'")
    fun getGeneralIncomeByMahal(mahalCode: String): Flow<Double?>

    @Query("SELECT SUM(amount) FROM cash_transactions WHERE mahalCode = :mahalCode AND type = 'EXPENSE'")
    fun getExpensesByMahal(mahalCode: String): Flow<Double?>

    @Query("SELECT * FROM cash_transactions ORDER BY transactionDate DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Flow<List<CashTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CashTransaction): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<CashTransaction>)

    @Update
    suspend fun updateTransaction(transaction: CashTransaction)

    @Delete
    suspend fun deleteTransaction(transaction: CashTransaction)
}
