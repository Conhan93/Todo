package com.example.todo.ui.TodoList.Components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todo.ui.TodoList.TodoListUIEvent


@Composable
fun NewTodoButton(
    onEvent: (TodoListUIEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { onEvent(TodoListUIEvent.addItem) },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Create new todo")
    }
}