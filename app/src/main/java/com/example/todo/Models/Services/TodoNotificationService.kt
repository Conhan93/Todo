package com.example.todo.Models.Services

import com.example.todo.Models.Todo
import java.time.LocalDateTime

interface TodoNotificationService {
    fun scheduleNotification(todo: Todo, dateTime: LocalDateTime)
}