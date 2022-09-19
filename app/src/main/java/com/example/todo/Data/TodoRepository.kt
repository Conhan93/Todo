package com.example.todo.Data

import com.example.todo.Models.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {


    suspend fun insertTodoAsync(todo : Todo)

    suspend fun deleteTodoAsync(todo: Todo)

    suspend fun getTodoByIDAsync(id : Int) : Todo?

    fun getTodos() : Flow<List<Todo>>
}