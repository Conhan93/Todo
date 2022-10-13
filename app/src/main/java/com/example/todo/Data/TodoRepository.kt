package com.example.todo.Data

import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TodoRepository {


    suspend fun insertTodoAsync(todo : Todo)

    suspend fun deleteTodoAsync(todo: Todo)

    suspend fun getTodoByIDAsync(id : Int) : Todo?

    fun getTodos() : Flow<List<Todo>>

    suspend fun insertReminderAsync(reminder: ReminderNotification): Boolean

    suspend fun deleteReminderAsync(reminder: ReminderNotification)

    suspend fun getReminderByIdAsync(id: Int): ReminderNotification?
    suspend fun getReminderByUUIDAsync(uuid: UUID): ReminderNotification?
    suspend fun getReminderByUUIDAsync(uuid: String): ReminderNotification?

    fun getAllReminders(): Flow<List<ReminderNotification>>
    fun getAllRemindersByTodoId(id: Int): Flow<List<ReminderNotification>>
}