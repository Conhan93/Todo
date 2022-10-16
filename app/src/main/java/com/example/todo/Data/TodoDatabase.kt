package com.example.todo.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo

@Database(
    entities = [Todo::class, ReminderNotification::class],
    version = 3,
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract val todoDAO: TodoDAO
    abstract val reminderDAO: ReminderDAO
}