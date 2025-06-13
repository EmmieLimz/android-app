package com.nipa.healthcareapp.model.chat

data class ChatConversation(
    val id: String = "",
    val patientId: String = "",
    val providerId: String = "",
    val lastMessage: Message? = null,
    val unreadCount: Int = 0
)
