package com.example.todo.Viewmodels

import com.example.todo.Models.Todo

class TodoItemVM(
    private var todo : Todo
) {

    private var title = todo.title
    private var body = todo.body

    fun getTitle() : String = title
    fun getBody() : String = body
    fun setBody(text : String) { body = text}
}