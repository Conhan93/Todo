package com.example.todo.Viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.todo.Models.Todo

class TodoListVM : ViewModel() {

    private val todos = mutableStateListOf(Todo(title = "Android", "babababa"))

    fun getTodos() : List<Todo> {
        return todos
    }
}