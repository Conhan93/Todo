package com.example.todo.Data

import androidx.room.*
import com.example.todo.Models.ReminderNotification
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ReminderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderAsync(reminder: ReminderNotification)

    @Delete
    suspend fun deleteReminderAsync(reminder: ReminderNotification)

    @Query("SELECT * FROM ReminderNotification WHERE id == :id")
    suspend fun getReminderByIdAsync(id: Int): ReminderNotification?


    suspend fun getReminderByUUIDAsync(uuid: UUID): ReminderNotification? {
        return  getReminderByUUIDAsync(uuid.toString())
    }

    @Query("SELECT * FROM ReminderNotification WHERE uuid == :uuid")
    suspend fun getReminderByUUIDAsync(uuid: String): ReminderNotification?

    @Query("SELECT * FROM ReminderNotification")
    fun getAllReminders(): Flow<List<ReminderNotification>>

    @Query("SELECT * FROM ReminderNotification WHERE todoId == :id")
    fun getAllRemindersByTodoId(id: Int): Flow<List<ReminderNotification>>
}