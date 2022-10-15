package com.example.todo.ui.TodoItem.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.ui.TodoItem.TodoItemEvent
import com.example.todo.ui.TodoItem.TodoItemVM


@Composable
fun TodoItemTopBar(
    todoTitle : String,
    editState: TodoItemVM.TodoState,
    onEvent: (TodoItemEvent) -> Unit
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
            if (editState == TodoItemVM.TodoState.EDIT) {
                val buttonWeight = Modifier.weight(1f)
                TextField(
                    value = todoTitle,
                    onValueChange = { onEvent(TodoItemEvent.setTitle(it)) },
                    modifier = padding.weight(3f)
                )
                IconButton(
                    onClick = { onEvent(TodoItemEvent.saveTodo) },
                    modifier = padding.then(buttonWeight)
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "save todo")
                }

            } else {
                Text(
                    text = todoTitle,
                    modifier = padding
                )
                Row {
                    IconButton(
                        onClick = { onEvent(TodoItemEvent.navigateToReminders) },
                    ) {
                        Icon(imageVector = Icons.Filled.DateRange, null)
                    }
                    Button(
                        onClick = { onEvent(TodoItemEvent.setEditState(TodoItemVM.TodoState.EDIT))},
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primaryVariant),
                        modifier = padding
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }
        }
    }
}