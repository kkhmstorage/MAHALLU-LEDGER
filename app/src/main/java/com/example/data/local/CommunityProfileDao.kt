package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CommunityProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityProfileDao {
    @Query("SELECT * FROM community_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<CommunityProfile?>

    @Query("SELECT * FROM community_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileSync(): CommunityProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: CommunityProfile)

    @Update
    suspend fun updateProfile(profile: CommunityProfile)
}
