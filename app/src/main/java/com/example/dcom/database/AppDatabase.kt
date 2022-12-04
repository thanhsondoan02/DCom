package com.example.dcom.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dcom.database.message.IMessageDao
import com.example.dcom.database.message.Message
import com.example.dcom.database.note.INoteDao
import com.example.dcom.database.note.Note

@Database(entities = [Note::class, Message::class],  version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun iNoteDao(): INoteDao
    abstract fun iMessageDao(): IMessageDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "my-sample-database"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }

}
