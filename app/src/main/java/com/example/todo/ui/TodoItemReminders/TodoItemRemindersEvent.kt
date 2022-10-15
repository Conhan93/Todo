package com.example.todo.ui.TodoItemReminders

import com.example.todo.Models.ReminderNotification
import java.time.ZonedDateTime

sealed class TodoItemRemindersEvent {
    object BackPressEvent: TodoItemRemindersEvent()
    object SaveEvent: TodoItemRemindersEvent()
    object NewReminderEvent: TodoItemRemindersEvent()
    data class AddEvent(val dateTime: ZonedDateTime): TodoItemRemindersEvent()
    data class DeleteEvent(val reminder: ReminderNotification): TodoItemRemindersEvent()
}
