package com.example.todo.ui.TodoItemReminders.Components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todo.ui.TodoItemReminders.TodoItemRemindersEvent
import com.example.todo.ui.TodoList.TodoListUIEvent

@Composable
fun NewReminderButton(
    modifier: Modifier = Modifier,
    onEvent: (TodoItemRemindersEvent) -> Unit
) {
    FloatingActionButton(
        onClick = { onEvent(TodoItemRemindersEvent.NewReminderEvent) },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Create new todo")
    }
}