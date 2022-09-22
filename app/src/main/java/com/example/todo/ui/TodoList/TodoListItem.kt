package com.example.todo.ui.TodoList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todo.Models.Todo


@Composable
fun TodoListItem(
    todoItem : Todo,
    modifier: Modifier = Modifier,
    onEvent : (TodoListUIEvent) -> Unit
) {
    Column(modifier.clickable { onEvent(TodoListUIEvent.itemSelect(todoItem.id!!)) }){
        Row {
            Text(
                text = todoItem.title,
                maxLines = 1
            )
        }
        Row(Modifier.padding(start = 5.dp)) {
            Text(
                text = todoItem.body,
                maxLines = 3,
                overflow = TextOverflow.Clip
            )
        }
    }
}