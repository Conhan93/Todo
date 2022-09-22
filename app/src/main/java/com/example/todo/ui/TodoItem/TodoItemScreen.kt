package com.example.todo.Views

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoItem.TodoItemEvent
import com.example.todo.ui.TodoItem.TodoItemVM



@Composable
fun TodoItemScreen(
    modifier : Modifier = Modifier,
    viewModel : TodoItemVM = hiltViewModel(),
    onPopBack : (UIEvent.popStackBack) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect {
            when(it) {
                is UIEvent.popStackBack -> onPopBack(it)
                else -> Unit
            }
        }
    }


    Scaffold(
        topBar = { topBar(todoTitle = viewModel._title, onEvent = viewModel::onEvent ) }
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel._body,
                onValueChange = { viewModel.onEvent(TodoItemEvent.setBody(it)) }
            )
        }
    }

}

@Composable
fun topBar(
    todoTitle : String,
    onEvent: (TodoItemEvent) -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 3.dp,
    ) {
        TextField(
            value = todoTitle,
            onValueChange = { onEvent(TodoItemEvent.setTitle(it)) }
        )
        Button(onClick = { onEvent(TodoItemEvent.saveTodo) }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "save todo")
        }
        Button(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.Menu, "Todo Menu")
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun TodoItemPreview() {
    TodoItemScreen { }
}