package com.example.todo.Models.Services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo
import com.example.todo.R
import com.example.todo.Util.Constants
import java.time.ZonedDateTime
import java.util.*


class TodoNotificationServiceImpl(
    private val repository: TodoRepository,
    private val context: Context
): TodoNotificationService {

    private val res = context.resources

    override suspend fun scheduleNotification(todo: Todo, dateTime: ZonedDateTime): UUID? {
        val intent = Intent(context, TodoNotificationReciever::class.java)

        val uuid = UUID.randomUUID()
        val reminderNotification = ReminderNotification(
            title = todo.title,
            message = todo.body,
            uuid = uuid.toString(),
            date = dateTime.toString(),
            todoId = todo.id!!
        )

        intent.putExtra(res.getString(R.string.notification_id_key), todo.title)
        intent.putExtra(res.getString(R.string.notification_message_key), todo.body)
        intent.putExtra(res.getString(R.string.notification_id_key), uuid.toString())

        if(repository.insertReminderAsync(reminderNotification)) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                uuid.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dateTime.toInstant().toEpochMilli(),
                pendingIntent
            )

            return uuid
        } else {
            Log.e(Constants.APP_TAG, "scheduleNotification: Unable to save reminder")
            return null
        }
    }

    override suspend fun removeNotification(reminderNotification: ReminderNotification): UUID? {
        val intent = Intent(context, TodoNotificationReciever::class.java)

        val uuid = UUID.fromString(reminderNotification.uuid)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uuid.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService<AlarmManager>()
        alarmManager?.cancel(pendingIntent)

        repository.deleteReminderAsync(reminderNotification)

        return uuid
    }


    companion object {
        const val TODO_REMINDER_CHANNEL_ID = "todo_reminder_channel"
    }
}