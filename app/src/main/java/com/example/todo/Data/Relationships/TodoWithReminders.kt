package com.example.todo.Data.Relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo

data class TodoWithReminders(
    @Embedded val todo: Todo,
    @Relation(parentColumn = "id", entityColumn = "todoId") val reminders: List<ReminderNotification>
)
