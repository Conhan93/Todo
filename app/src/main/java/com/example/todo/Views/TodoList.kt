package com.example.todo.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.Models.Todo
import com.example.todo.Viewmodels.TodoListVM
import com.example.todo.state.State


@Composable
fun TodoList(state : MutableState<State>) {

    val tm by remember { mutableStateOf( TodoListVM() ) }
    
    Scaffold(
        topBar = {
            TopAppBar {
                Text(
                    text = "Todo",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
                Button(onClick = { state.value = State.Item(Todo("New", ""))}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create new todo")
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),

            ) {
                tm
                    .getTodos()
                    .forEach {
                        Row(Modifier.padding(start = 10.dp)){
                            Text(
                                text = it.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { state.value = State.Item(it) }
                            )
                        }
                    }
            }
        }
    )


}




@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun TodoListPreview() {
    val dummyState = mutableStateOf<State>(State.ShowList)
    TodoList(dummyState)
}