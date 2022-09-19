package com.example.todo.Data

import com.example.todo.Models.Todo
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(
    private val dao : TodoDAO
) : TodoRepository {
    override suspend fun insertTodoAsync(todo: Todo) {
        dao.insertTodoAsync(todo)
    }

    override suspend fun deleteTodoAsync(todo: Todo) {
        dao.deleteTodoAsync(todo)
    }

    override suspend fun getTodoByIDAsync(id: Int): Todo? {
        return dao.getTodoByIDAsync(id)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos()
    }
}