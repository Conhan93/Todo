package com.example.todo.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    var title: String,
    var body: String,
    var isCompleted: Boolean = false,
    @PrimaryKey val id: Int? = null
)
