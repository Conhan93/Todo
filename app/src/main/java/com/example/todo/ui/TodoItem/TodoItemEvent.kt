package com.example.todo.ui.TodoItem

sealed class TodoItemEvent {
    data class setBody(val body: String) : TodoItemEvent()
    data class setTitle(val title: String) : TodoItemEvent()
    data class setEditState(val state: TodoItemVM.TodoState) : TodoItemEvent()
    object saveTodo : TodoItemEvent()
    object navigateToReminders: TodoItemEvent()
}
