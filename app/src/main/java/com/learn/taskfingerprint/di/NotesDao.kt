package com.learn.taskfingerprint.di

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learn.taskfingerprint.business.Note

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes():LiveData<List<Note>>

}