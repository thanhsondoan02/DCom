package com.example.dcom.database.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "is_me") val isMine: Boolean,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "create_time") val createTime: Long,
) {
    constructor(isMe: Boolean, title: String, createTime: Long) : this(0, isMe, title, createTime)
}

