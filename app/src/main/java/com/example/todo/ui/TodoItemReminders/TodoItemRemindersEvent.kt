package com.example.todo.ui.TodoItemReminders

import com.example.todo.Models.ReminderNotification

sealed class TodoItemRemindersEvent {
    object BackPressEvent: TodoItemRemindersEvent()
    object SaveEvent: TodoItemRemindersEvent()
    data class DeleteEvent(val reminder: ReminderNotification): TodoItemRemindersEvent()
    data class EditEvent(val reminder: ReminderNotification): TodoItemRemindersEvent()
    data class AddEvent(val reminder: ReminderNotification): TodoItemRemindersEvent()
}
