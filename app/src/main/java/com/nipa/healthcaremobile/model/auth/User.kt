package com.nipa.healthcaremobile.model.auth

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val id: String = "", // Assuming id is Firebase UID
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "user_type") val userType: String = "" // "Client" or "HealthcareProvider"
    // Add other common fields if they need to be stored
)
