package com.example.todo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.todo.Models.Services.TodoNotificationServiceImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        createTodoNotificationChannel()
    }

    private fun createTodoNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val todoReminderChannel = NotificationChannel(
                TodoNotificationServiceImpl.TODO_REMINDER_CHANNEL_ID,
                "Todo Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            todoReminderChannel.description = "Used to issue reminders of todos"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(todoReminderChannel)
        }
    }
}