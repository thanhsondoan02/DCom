package com.example.dcom.database.conversation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Conversation(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "conversation_id") val name: String,
    @ColumnInfo(name = "latest_message_id") val lastMessageId: Int,
) {
    constructor(name: String, lastMessageId: Int) : this(0, name, lastMessageId)
}

