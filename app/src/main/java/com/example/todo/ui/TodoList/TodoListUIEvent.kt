package com.example.todo.ui.TodoList

sealed class TodoListUIEvent {
    data class itemSelect(val id : Int) : TodoListUIEvent()
    data class itemDelete(val id : Int) : TodoListUIEvent()
    object addItem : TodoListUIEvent()
}
