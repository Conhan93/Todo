package com.example.todo.Views

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.R


import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoList.Components.NewTodoButton
import com.example.todo.ui.TodoList.Components.TodoFilterButtons
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
    val filter = vm.filterState

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
                    text = stringResource(id = R.string.todo_list_header),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .testTag(stringResource(R.string.test_todo_list_header))
                )
            }
        },
        floatingActionButton = { NewTodoButton(onEvent = vm::onUIEvent) }
    ) {
        Column(Modifier.fillMaxSize()) {
            TodoFilterButtons(onEvent = vm::onUIEvent, filter = filter)
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
                                .testTag("${stringResource(R.string.test_todo_list_item)}/${it.title}")
                        )
                    }

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