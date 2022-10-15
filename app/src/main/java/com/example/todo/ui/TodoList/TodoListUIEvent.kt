package com.example.todo.ui.TodoList

sealed class TodoListUIEvent {
    data class itemSelect(val id : Int) : TodoListUIEvent()
    data class itemDelete(val id : Int) : TodoListUIEvent()
    data class itemCheck(val id: Int, val check: Boolean): TodoListUIEvent()
    object addItem : TodoListUIEvent()
}
