package com.example.todo.ui.TodoItemReminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoItem.TodoItemVM
import com.example.todo.ui.TodoItemReminders.Components.TodoItemRemindersTopBar
import com.example.todo.ui.TodoList.SwipeableListItem


@Composable
fun TodoItemRemindersScreen(
    modifier : Modifier = Modifier,
    viewModel : TodoItemRemindersVM = hiltViewModel(),
    onPopBack : (UIEvent.popStackBack) -> Unit
) {
    val reminders = viewModel.reminders.collectAsState(initial = listOf())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect {
            when(it) {
                is UIEvent.popStackBack -> onPopBack(it)
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = { TodoItemRemindersTopBar(onEvent = viewModel::onEvent)}
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(start = 4.dp)
        ) {
            items(reminders.value, key = {it.id!!}) {
                SwipeableListItem(onDismiss = { viewModel.onEvent(TodoItemRemindersEvent.DeleteEvent(it)) }) {
                    Text(it.date)
                }
            }
        }
    }
}