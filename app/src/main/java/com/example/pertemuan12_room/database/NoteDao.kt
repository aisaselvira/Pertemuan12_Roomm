package com.example.pertemuan12_room.database

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Notes)

    @Update
    fun update(note: Notes)

    @Delete
    fun delete(notes: Notes)

    @get:Query("SELECT * from note_table ORDER BY id ASC")
    val allNotes: LiveData<List<Notes>>


}