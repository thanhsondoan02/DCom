package com.example.dcom.database.conversation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dcom.database.message.Message

@Dao
interface IConversationDao {
    @Query("SELECT * FROM message WHERE conversation_id = :conversationId")
    fun getByConversationId(conversationId: Int): List<Message>

    @Query("SELECT * FROM message WHERE conversation_id = :conversationId ORDER BY id DESC LIMIT 1")
    fun getLatestMessageInConversation(conversationId: Int): Message

    @Insert
    fun insert(vararg messages: Message)

    @Insert
    fun insertConversation(vararg conversations: Conversation)

    @Insert
    fun insertMessages(messages: List<Message>)

    @Query("SELECT * FROM conversation")
    fun getAll(): List<Conversation>

    @Query("SELECT * FROM message WHERE conversation_id = :conversationId ORDER BY id DESC LIMIT 100")
    fun getLatest100Messages(conversationId: Int): List<Message>

    @Query("SELECT COUNT(*) FROM message WHERE conversation_id = :conversationId")
    fun getSizeMessages(conversationId: Int): Int

    @Query("SELECT * FROM message WHERE conversation_id = :conversationId AND id < :lastMessageId ORDER BY id DESC LIMIT 100")
    fun getLatest100Messages(conversationId: Int, lastMessageId: Int): List<Message>

    @Query("SELECT * FROM conversation ORDER BY id DESC LIMIT 1")
    fun getLatestConversation(): Conversation

    @Query("SELECT * FROM message WHERE conversation_id = :conversationId ORDER BY id DESC")
    fun getAllMessageInConversationSortByLatest(conversationId: Int): List<Message>

    @Query("SELECT * FROM message WHERE conversation_id = :conversationId")
    fun getAllMessageInConversation(conversationId: Int): List<Message>

}
