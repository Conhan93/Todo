package com.example.todo.Data

import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo
import com.example.todo.Util.isUUID
import kotlinx.coroutines.flow.Flow
import java.util.*

class TodoRepositoryImpl(
    private val todoDAO : TodoDAO,
    private val reminderDAO: ReminderDAO
) : TodoRepository {
    override suspend fun insertTodoAsync(todo: Todo) {
        todoDAO.insertTodoAsync(todo)
    }

    override suspend fun deleteTodoAsync(todo: Todo) {
        todoDAO.deleteTodoAsync(todo)
    }

    override suspend fun getTodoByIDAsync(id: Int): Todo? {
        return todoDAO.getTodoByIDAsync(id)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return todoDAO.getTodos()
    }

    override suspend fun insertReminderAsync(reminder: ReminderNotification): Boolean {
        if (todoDAO.getTodoByIDAsync(reminder.todoId) != null) {
            reminderDAO.insertReminderAsync(reminder)
            return true
        }

        return false
    }

    override suspend fun deleteReminderAsync(reminder: ReminderNotification) {
        reminderDAO.deleteReminderAsync(reminder)
    }

    override suspend fun getReminderByIdAsync(id: Int): ReminderNotification? {
        return reminderDAO.getReminderByIdAsync(id)
    }

    override suspend fun getReminderByUUIDAsync(uuid: UUID): ReminderNotification? {
        return reminderDAO.getReminderByUUIDAsync(uuid)
    }

    override suspend fun getReminderByUUIDAsync(uuid: String): ReminderNotification? {
        return if (uuid.isUUID()) reminderDAO.getReminderByUUIDAsync(uuid) else null
    }

    override fun getAllReminders(): Flow<List<ReminderNotification>> {
        return reminderDAO.getAllReminders()
    }

    override fun getAllRemindersByTodoId(id: Int): Flow<List<ReminderNotification>> {
        return reminderDAO.getAllRemindersByTodoId(id)
    }
}