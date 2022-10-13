package com.example.todo.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderNotification(
    val title: String,
    val message: String,
    val uuid: String,
    val todoId: Int,
    val date: String,
    @PrimaryKey val id: Int? = null
)
