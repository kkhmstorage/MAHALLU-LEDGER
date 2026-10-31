package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MahalCommunity
import kotlinx.coroutines.flow.Flow

@Dao
interface MahalCommunityDao {
    @Query("SELECT * FROM mahal_communities ORDER BY name ASC")
    fun getAllCommunities(): Flow<List<MahalCommunity>>

    @Query("SELECT * FROM mahal_communities WHERE mahalCode = :code LIMIT 1")
    suspend fun getCommunityByCode(code: String): MahalCommunity?

    @Query("SELECT * FROM mahal_communities WHERE subAdminUsername = :username LIMIT 1")
    suspend fun getCommunityByUsername(username: String): MahalCommunity?

    @Query("SELECT * FROM mahal_communities WHERE (subAdminUsername = :username OR mahalCode = :username) AND subAdminPassword = :password AND isActive = 1 LIMIT 1")
    suspend fun authenticateSubAdmin(username: String, password: String): MahalCommunity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunity(community: MahalCommunity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(communities: List<MahalCommunity>)

    @Update
    suspend fun updateCommunity(community: MahalCommunity)

    @Delete
    suspend fun deleteCommunity(community: MahalCommunity)

    @Query("SELECT COUNT(*) FROM mahal_communities")
    fun getCommunityCount(): Flow<Int>
}
