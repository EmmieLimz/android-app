package com.nipa.healthcaremobile.viewmodel.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nipa.healthcaremobile.model.chat.Message // Assuming Message is in this path

class ChatViewModel : ViewModel() {
    init {
        Log.d("ChatViewModel", "ChatViewModel initialized")
    }

    fun loadMessages(conversationId: String) {
        Log.i("ChatViewModel", "loadMessages for conversation: \$conversationId")
        // TODO: Implement logic to load messages using ChatRepository
        // TODO: Update LiveData with the list of messages
    }

    fun sendMessage(message: Message) {
        Log.i("ChatViewModel", "sendMessage: \${message.text}")
        // TODO: Implement logic to send message using ChatRepository
    }
}
