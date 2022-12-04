package com.example.dcom.database.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IMessageDao {
    @Query("SELECT * FROM message")
    fun getAll(): List<Message>

    @Query("SELECT * FROM message WHERE id IN (:messageIds)")
    fun loadAllByIds(messageIds: IntArray): List<Message>

    @Insert
    fun insert(vararg users: Message)

    @Insert
    fun insertAll(messages: List<Message>)

    @Query("UPDATE message SET content = :content WHERE id = :id")
    fun update(content: String, id: Int)

    @Query("SELECT * FROM message WHERE id = :id")
    fun get(id: Int): Message

    @Query("SELECT * FROM message ORDER BY id DESC LIMIT 1")
    fun getLatestMessage(): Message

    @Query("SELECT id FROM message ORDER BY id DESC LIMIT 1")
    fun getLatestMessageId(): Int

    @Query("SELECT * FROM message ORDER BY id DESC LIMIT :size")
    fun getLatestMessages(size: Int): List<Message>

    @Delete
    fun delete(user: Message)

    @Query("DELETE FROM message WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM message")
    fun deleteAll()

    @Query("SELECT * FROM message WHERE content LIKE '%' || :keyword || '%'")
    fun search(keyword: String): List<Message>

}

