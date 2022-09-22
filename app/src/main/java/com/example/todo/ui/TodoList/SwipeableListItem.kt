package com.example.todo.ui.TodoList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableListItem(
    onDismiss : () -> Unit,
    listItem : @Composable RowScope.() -> Unit
) {
    val dismissState = rememberDismissState(DismissValue.Default) {
        if (it == DismissValue.DismissedToEnd)
            onDismiss()

        true
    }


    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        background = {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(Color.Red)
            ) {
                Row {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete item")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Delete item")
                }
            }

        },
        dismissContent = listItem
    )
}