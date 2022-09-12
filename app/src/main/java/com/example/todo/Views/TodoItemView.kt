package com.example.todo.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.Models.Todo
import com.example.todo.Viewmodels.TodoItemVM
import com.example.todo.state.State


@Composable
fun TodoItem(
    state : MutableState<State>,
    modifier : Modifier = Modifier
) {
    val vm by remember { mutableStateOf(TodoItemVM((state.value as State.Item).item))  }

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
                    Text(vm.getTitle())
                }
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = vm.getBody(),
                        onValueChange = { vm.setBody(it) }
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
    val dummyTodo = Todo(title = "Foo", "Fooby foobdy dooby doo")
    val dummyState = mutableStateOf<State>(State.Item(dummyTodo))
    TodoItem(dummyState)
}