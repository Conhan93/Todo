package com.example.todo.Data

import androidx.room.*
import com.example.todo.Models.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoAsync(todo : Todo)
    @Delete
    suspend fun deleteTodoAsync(todo: Todo)
    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoByIDAsync(id : Int) : Todo?
    @Query("SELECT * FROM todo")
    suspend fun getTodosAsync() : Flow<List<Todo>>
}