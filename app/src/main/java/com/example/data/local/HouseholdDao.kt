package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Household
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseholdDao {
    @Query("SELECT * FROM households WHERE isActive = 1 ORDER BY houseNumber ASC")
    fun getAllHouseholds(): Flow<List<Household>>

    @Query("SELECT * FROM households WHERE mahalCode = :mahalCode AND isActive = 1 ORDER BY houseNumber ASC")
    fun getHouseholdsByMahal(mahalCode: String): Flow<List<Household>>

    @Query("SELECT * FROM households WHERE id = :id LIMIT 1")
    suspend fun getHouseholdById(id: Long): Household?

    @Query("SELECT * FROM households WHERE mahalCode = :mahalCode AND houseNumber = :houseNumber LIMIT 1")
    suspend fun getHouseholdByNumber(mahalCode: String, houseNumber: String): Household?

    @Query("SELECT * FROM households WHERE phoneNumber = :phone AND isActive = 1 LIMIT 1")
    suspend fun getHouseholdByPhone(phone: String): Household?

    @Query("SELECT * FROM households WHERE phoneNumber LIKE '%' || :phone || '%' OR houseNumber LIKE '%' || :phone || '%'")
    suspend fun searchHousehold(phone: String): List<Household>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHousehold(household: Household): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(households: List<Household>)

    @Update
    suspend fun updateHousehold(household: Household)

    @Delete
    suspend fun deleteHousehold(household: Household)

    @Query("SELECT COUNT(*) FROM households WHERE isActive = 1")
    fun getHouseholdCount(): Flow<Int>

    @Query("SELECT * FROM households WHERE mahalCode = :mahalCode AND verificationStatus = 'PENDING_VERIFICATION' AND isActive = 1 ORDER BY houseNumber ASC")
    fun getPendingVerifications(mahalCode: String): Flow<List<Household>>

    @Query("UPDATE households SET verificationStatus = :status, verifiedBy = :verifiedBy, verifiedDate = :verifiedDate WHERE id = :householdId")
    suspend fun updateVerificationStatus(householdId: Long, status: String, verifiedBy: String, verifiedDate: Long)
}
