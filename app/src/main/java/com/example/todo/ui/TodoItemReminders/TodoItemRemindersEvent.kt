package com.example.todo.ui.TodoItemReminders

sealed class TodoItemRemindersEvent {
    object backPressEvent: TodoItemRemindersEvent()
    object saveEvent: TodoItemRemindersEvent()
}
