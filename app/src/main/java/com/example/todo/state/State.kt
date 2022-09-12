package com.example.todo.state

import com.example.todo.Models.Todo

sealed class State {
    object ShowList : State()
    data class Error(val cause : String?) : State()
    data class Item(val item : Todo) : State()
}
