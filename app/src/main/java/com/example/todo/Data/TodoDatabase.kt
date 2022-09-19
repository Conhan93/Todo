package com.example.todo.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.Models.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract val dao : TodoDAO
}