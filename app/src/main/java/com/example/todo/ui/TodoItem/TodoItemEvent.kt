package com.example.todo.ui.TodoItem

sealed class TodoItemEvent {
    data class setBody(val body : String) : TodoItemEvent()
    data class setTitle(val title : String) : TodoItemEvent()
    object saveTodo : TodoItemEvent()
}
