package com.nipa.healthcareapp.model.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index
import com.nipa.healthcareapp.model.auth.User // For ForeignKey

@Entity(tableName = "messages",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"], // user_id from User table (updated User.kt)
        childColumns = ["sender_id"],
        onDelete = ForeignKey.SET_NULL // Or CASCADE, RESTRICT, etc.
    )],
    indices = [Index(value = ["conversation_id"]), Index(value = ["timestamp"]), Index(value = ["sender_id"])]
)
data class Message(
    @PrimaryKey @ColumnInfo(name = "message_id") val id: String = "", // Assuming message ID is provided
    @ColumnInfo(name = "conversation_id") val conversationId: String = "",
    @ColumnInfo(name = "sender_id") val senderId: String? = null, // Nullable if sender can be deleted
    @ColumnInfo(name = "receiver_id") val receiverId: String = "", // Not linking this via FK for now for simplicity
    @ColumnInfo(name = "text_content") val text: String = "", // Renamed 'text' to avoid SQL keyword conflicts
    @ColumnInfo(name = "timestamp") val timestamp: Long = 0L,
    @ColumnInfo(name = "is_read") val isRead: Boolean = false
)
