package com.example.todo.Viewmodels

sealed class TodoItemEvent {
    data class setBody(val body : String) : TodoItemEvent()
    data class setTitle(val title : String) : TodoItemEvent()
    object saveTodo : TodoItemEvent()
}
