package com.example.todo.ui.TodoItemReminders.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.Models.ReminderNotification
import com.example.todo.ui.TodoItemReminders.TodoItemRemindersEvent
import com.example.todo.ui.TodoList.SwipeableListItem

@Composable
fun RemindersList(
    modifier: Modifier = Modifier,
    reminders: List<ReminderNotification>,
    onEvent: (TodoItemRemindersEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(reminders, key = {it.id!!}) {
            SwipeableListItem(onDismiss = { onEvent(TodoItemRemindersEvent.DeleteEvent(it)) }) {
                ReminderCard(
                    reminderNotification = it,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}