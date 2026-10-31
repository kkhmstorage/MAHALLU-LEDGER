package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AdditionalCollection
import com.example.data.model.AdditionalCollectionPayment
import kotlinx.coroutines.flow.Flow

@Dao
interface AdditionalCollectionDao {
    @Query("SELECT * FROM additional_collections WHERE mahalCode = :mahalCode AND isActive = 1 ORDER BY id DESC")
    fun getCollectionsByMahal(mahalCode: String): Flow<List<AdditionalCollection>>

    @Query("SELECT * FROM additional_collections WHERE id = :id LIMIT 1")
    suspend fun getCollectionById(id: Long): AdditionalCollection?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: AdditionalCollection): Long

    @Update
    suspend fun updateCollection(collection: AdditionalCollection)

    @Delete
    suspend fun deleteCollection(collection: AdditionalCollection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: AdditionalCollectionPayment): Long

    @Query("SELECT * FROM additional_collection_payments WHERE collectionId = :collectionId ORDER BY paymentDate DESC")
    fun getPaymentsByCollection(collectionId: Long): Flow<List<AdditionalCollectionPayment>>

    @Query("SELECT * FROM additional_collection_payments WHERE householdId = :householdId ORDER BY paymentDate DESC")
    fun getPaymentsByHousehold(householdId: Long): Flow<List<AdditionalCollectionPayment>>

    @Query("SELECT * FROM additional_collection_payments WHERE mahalCode = :mahalCode ORDER BY paymentDate DESC")
    fun getAllPaymentsByMahal(mahalCode: String): Flow<List<AdditionalCollectionPayment>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM additional_collection_payments WHERE collectionId = :collectionId")
    fun getTotalCollectedForCollection(collectionId: Long): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM additional_collection_payments WHERE collectionId = :collectionId AND householdId = :householdId")
    fun getHouseholdTotalForCollection(collectionId: Long, householdId: Long): Flow<Double>
}
