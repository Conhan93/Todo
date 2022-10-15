package com.example.todo.ui.TodoItemReminders.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.ui.TodoItemReminders.TodoItemRemindersEvent


@Composable
fun TodoItemRemindersTopBar(
    onEvent: (TodoItemRemindersEvent) -> Unit
) {
    val padding = Modifier.padding(3.dp)

    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 3.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEvent(TodoItemRemindersEvent.BackPressEvent) },
                padding
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }
    }
}