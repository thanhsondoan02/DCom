package com.example.dcom.database.conversation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Conversation(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "created_time") val createdTime: Long,

) {
    constructor(name: String, createdTime: Long) : this(0, name, createdTime)
}

