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
import androidx.hilt.navigation.compose.hiltViewModel


import com.example.todo.Util.UIEvent
import com.example.todo.Viewmodels.TodoListUIEvent
import com.example.todo.Viewmodels.TodoListVM



@Composable
fun TodoList(
    vm : TodoListVM = hiltViewModel(),
    onNavigate : (UIEvent.navigate) -> Unit,
) {

    val todos = vm.todos.collectAsState(initial = listOf())

    LaunchedEffect(key1 = true) {
        vm.uiEvents.collect {
            when(it) {
                is UIEvent.navigate -> onNavigate(it)
                else -> Unit
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar {
                Text(
                    text = "Todo",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
                Button(onClick = { vm.onUIEvent(TodoListUIEvent.addItem) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create new todo")
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),

            ) {
                todos.value.forEach {
                    Row(Modifier.padding(start = 10.dp)) {
                        Text(
                            text = it.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { vm.onUIEvent(TodoListUIEvent.itemSelect(it.id!!)) }
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
    TodoList() {}
}