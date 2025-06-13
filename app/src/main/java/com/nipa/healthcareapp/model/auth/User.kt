package com.nipa.healthcareapp.model.auth

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val id: String = "", // This will store Firebase UID
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "user_type") val userType: String = "" // e.g., "client" or "provider"
) {
    // While data classes with all properties having default values have an implicit no-arg constructor,
    // sometimes explicit declaration is clearer or helps with certain tools.
    // However, for Room and Firestore with default args, it's usually not strictly needed.
    // constructor() : this("", "", "", "") // This line is redundant if all properties have defaults.
}
