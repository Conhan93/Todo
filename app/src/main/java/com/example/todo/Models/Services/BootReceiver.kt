package com.example.todo.Models.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import com.example.todo.Data.TodoRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject
    lateinit var repository: TodoRepository
    @Inject
    lateinit var notificationService: TodoNotificationService

    @CallSuper
    override fun onReceive(context: Context?, intent: Intent?) {

        GlobalScope.launch {
            val reminders = repository.getAllReminders().first()

            reminders
                .associate { ZonedDateTime.parse(it.date) to repository.getTodoByIDAsync(it.todoId) }
                .forEach { notificationService.scheduleNotification(it.value!!, it.key) }
        }

    }
}