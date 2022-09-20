package com.example.todo.Viewmodels

import com.example.todo.Models.Todo

sealed class TodoListUIEvent {
    data class itemSelect(val id : Int) : TodoListUIEvent()
    data class itemDelete(val id : Int) : TodoListUIEvent()
    object addItem : TodoListUIEvent()
}
