package com.example.todo.Views

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.todo.Util.UIEvent
import com.example.todo.Viewmodels.TodoItemEvent
import com.example.todo.Viewmodels.TodoItemVM



@Composable
fun TodoItem(
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

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    elevation = 3.dp
                ) {
                    TextField(
                        value = viewModel._title,
                        onValueChange = { viewModel.onEvent(TodoItemEvent.setTitle(it)) }
                    )
                    Button(onClick = { viewModel.onEvent(TodoItemEvent.saveTodo) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "save todo")
                    }
                }
            },
            content = {
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
        ) // End scaffold
    }

}


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun TodoItemPreview() {
    TodoItem { }
}