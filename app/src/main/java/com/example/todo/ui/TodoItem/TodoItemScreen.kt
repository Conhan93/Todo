package com.example.todo.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoItem.Components.TodoItemTopBar
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
        topBar = { TodoItemTopBar(
            todoTitle = viewModel.title,
            onEvent = viewModel::onEvent,
            editState = viewModel.state
        ) }
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel.body,
                onValueChange = { viewModel.onEvent(TodoItemEvent.setBody(it)) }
            )
        }
    }

}


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun TodoItemPreview() {
    TodoItemScreen { }
}