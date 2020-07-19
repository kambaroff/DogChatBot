package com.example.android.dogchatbot.model

data class ChatMessage(
    val isMine: Boolean,
    val content: String,
    val isImage: Boolean
)
