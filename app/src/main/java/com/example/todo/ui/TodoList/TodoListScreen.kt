package com.example.todo.Views

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel



import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoList.Components.NewTodoButton
import com.example.todo.ui.TodoList.Components.TodoItemCard
import com.example.todo.ui.TodoList.SwipeableListItem
import com.example.todo.ui.TodoList.TodoListUIEvent
import com.example.todo.ui.TodoList.TodoListVM



@Composable
fun TodoListScreen(
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
            }
        },
        floatingActionButton = { NewTodoButton(onEvent = vm::onUIEvent) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp)
            ) {
            items(todos.value, key = { it.id!! }) {
                SwipeableListItem({vm.onUIEvent(TodoListUIEvent.itemDelete(it.id!!))}) {
                    TodoItemCard(
                        todo = it,
                        onEvent = vm::onUIEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

            }
        }
    }
}




@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun TodoListPreview() {
    TodoListScreen() {}
}