package com.example.todo.ui.TodoList.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.example.todo.Models.Todo
import com.example.todo.ui.TodoList.TodoListUIEvent
import com.example.todo.R


@Composable
fun TodoItemCard(
    modifier: Modifier = Modifier,
    todo: Todo,
    onEvent: (TodoListUIEvent) -> Unit
) {
    Surface(
        Modifier
            .clickable { onEvent(TodoListUIEvent.itemSelect(todo.id!!)) }
            .then(modifier),
        color = MaterialTheme.colors.primary,
        elevation = 5.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(Modifier.padding(start = 2.dp)) {
                    Text(
                        text = todo.title,
                        maxLines = 1,
                        style = MaterialTheme.typography.h6
                    )
                }
                Row(Modifier.padding(start = 5.dp)) {
                    Text(
                        text = todo.body,
                        maxLines = 3,
                        overflow = TextOverflow.Clip
                    )
                }
            }

            IconButton(
                onClick = { onEvent(TodoListUIEvent.itemCheck(todo.id!!, todo.isCompleted.not()))},
                modifier = Modifier
                    .testTag("${stringResource(R.string.test_todo_item_check_box)}/${todo.title}")
            ) {
                val iconTint = if (todo.isCompleted) {
                    MaterialTheme.colors.primaryVariant
                } else {
                    MaterialTheme.colors.onPrimary
                }
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = iconTint
                )
            }
        }
    }
}