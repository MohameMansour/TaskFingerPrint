package com.learn.taskfingerprint.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learn.taskfingerprint.business.Note

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteDatabase :RoomDatabase(){
    abstract fun getNotesDao(): NotesDao

    companion object {

        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instant =
                    Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java, "note_database").build()
                INSTANCE = instant
                instant
            }
        }

    }
}