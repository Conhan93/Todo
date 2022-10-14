package com.example.todo.ui.TodoItemReminders

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoItem.TodoItemVM
import com.example.todo.ui.TodoItemReminders.Components.TodoItemRemindersTopBar


@Composable
fun TodoItemRemindersScreen(
    modifier : Modifier = Modifier,
    viewModel : TodoItemRemindersVM = hiltViewModel(),
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
        topBar = { TodoItemRemindersTopBar(onEvent = viewModel::onEvent)}
    ) {

    }
}