package com.example.todo.ui.TodoList.Components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.todo.R
import com.example.todo.ui.TodoList.TodoListUIEvent
import com.example.todo.ui.TodoList.TodoListVM

@Composable
fun TodoFilterButtons(
    modifier: Modifier = Modifier,
    filter: TodoListVM.TodoFilter,
    onEvent: (TodoListUIEvent) -> Unit
) {
    val activeColor = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
    val inActiveColor = ButtonDefaults.buttonColors(MaterialTheme.colors.primary.copy(alpha = 0.8f))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        val buttonWeight = Modifier.weight(1f)
        Button(
            onClick = { onEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.DONE)) },
            modifier = buttonWeight
                .height(IntrinsicSize.Min)
                .testTag(stringResource(R.string.test_todo_list_done_button)),
            shape = RectangleShape,
            colors = if (filter == TodoListVM.TodoFilter.DONE) activeColor else inActiveColor
        ) {
            Text(
                stringResource(id = R.string.todo_filter_button_done),
                color = MaterialTheme.colors.onPrimary
            )
        }
        Button(
            onClick = { onEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.NOT_DONE)) },
            modifier = buttonWeight
                .height(IntrinsicSize.Min)
                .testTag(stringResource(R.string.test_todo_list_not_done_button)),
            shape = RectangleShape,
            colors = if (filter == TodoListVM.TodoFilter.NOT_DONE) activeColor else inActiveColor
        ) {
            Text(
                stringResource(id = R.string.todo_filter_button_not_done),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}