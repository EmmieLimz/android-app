package com.nipa.healthcaremobile.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nipa.healthcaremobile.model.auth.User
import kotlinx.coroutines.flow.Flow // Optional, for reactive queries

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): User?

    // Example of a Flow query
    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    fun getUserByIdFlow(userId: String): Flow<User?>
}
