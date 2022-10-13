package com.example.todo.Models.Services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import androidx.core.app.NotificationCompat
import com.example.todo.R

class TodoNotificationReciever: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val res = context.resources

        val notificationTitle = intent.getStringExtra(res.getString(R.string.notification_title_key))
        val notificationMessage = intent.getStringExtra(res.getString(R.string.notification_message_key))
        val notificationId = intent.getStringExtra(res.getString(R.string.notification_id_key))


        val notification = NotificationCompat
            .Builder(context, TodoNotificationServiceImpl.TODO_REMINDER_CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setContentText(notificationMessage)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(0, notification)
    }
}