package com.nipa.healthcaremobile.view.chat

import android.view.LayoutInflater // Add this import
import android.view.ViewGroup
import android.widget.TextView // Add this import
import androidx.recyclerview.widget.RecyclerView
import com.nipa.healthcaremobile.R // Add this import if not present
import com.nipa.healthcaremobile.model.chat.Message

// Basic ViewHolder, actual implementation will vary
class ChatMessageViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    val messageText: TextView = itemView.findViewById(R.id.tv_message_text) // Example view
}

class ChatListAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<ChatMessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return ChatMessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.text // Example binding
    }

    override fun getItemCount(): Int = messages.size
}
