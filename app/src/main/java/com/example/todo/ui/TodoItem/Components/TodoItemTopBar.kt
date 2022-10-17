package com.example.todo.ui.TodoItem.Components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.ui.TodoItem.TodoItemEvent
import com.example.todo.ui.TodoItem.TodoItemVM
import com.example.todo.R

@Composable
fun TodoItemTopBar(
    todoTitle : String,
    editState: TodoItemVM.TodoState,
    onEvent: (TodoItemEvent) -> Unit
) {

    val padding = Modifier.padding(3.dp)
    val titleStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )

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
                onClick = { onEvent(TodoItemEvent.BackPress)},
                modifier = Modifier.testTag(stringResource(R.string.test_todo_item_back_button))
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
            if (editState == TodoItemVM.TodoState.EDIT) {
                val buttonWeight = Modifier.weight(1f)
                TextField(
                    value = todoTitle,
                    onValueChange = { onEvent(TodoItemEvent.setTitle(it)) },
                    textStyle = titleStyle,
                    modifier = padding
                        .weight(3f)
                        .testTag(stringResource(R.string.test_todo_title_box))
                )
                IconButton(
                    onClick = { onEvent(TodoItemEvent.saveTodo) },
                    modifier = padding
                        .testTag(stringResource(R.string.test_todo_item_save_button))
                        .then(buttonWeight)
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "save todo")
                }

            } else {
                Text(
                    text = todoTitle,
                    style = titleStyle,
                    modifier = padding.testTag(stringResource(R.string.test_todo_title_box))
                )
                Row {
                    IconButton(
                        onClick = { onEvent(TodoItemEvent.navigateToReminders) },
                        modifier = Modifier.testTag(stringResource(R.string.test_todo_reminder_button))
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