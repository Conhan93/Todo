package com.example.todo.Models.Services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todo.Models.Todo
import java.time.LocalDateTime
import java.time.ZonedDateTime


class TodoNotificationServiceImpl(
    private val context: Context
): TodoNotificationService {


    override fun scheduleNotification(todo: Todo, dateTime: LocalDateTime) {
        val intent = Intent(context, TodoNotificationReciever::class.java)


        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateTime.toEpochSecond(ZonedDateTime.now().offset),
            pendingIntent
        )
    }


    companion object {
        const val TODO_REMINDER_CHANNEL_ID = "todo_reminder_channel"
    }
}