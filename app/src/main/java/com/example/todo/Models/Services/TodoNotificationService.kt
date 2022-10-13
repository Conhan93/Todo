package com.example.todo.Models.Services

import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

interface TodoNotificationService {
    suspend fun scheduleNotification(todo: Todo, dateTime: ZonedDateTime): UUID?
    suspend fun removeNotification(reminderNotification: ReminderNotification): UUID?
}