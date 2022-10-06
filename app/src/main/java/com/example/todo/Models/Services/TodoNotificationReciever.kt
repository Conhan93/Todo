package com.example.todo.Models.Services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class TodoNotificationReciever: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat
            .Builder(context, TodoNotificationServiceImpl.TODO_REMINDER_CHANNEL_ID)
            .setContentTitle("Reminder!!")
            .setContentText("Do the thing")
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(0, notification)
    }
}