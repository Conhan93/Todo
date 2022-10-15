package com.example.todo.ui.TodoItemReminders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
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
import com.example.todo.ui.TodoItemReminders.Components.ReminderCard
import com.example.todo.ui.TodoItemReminders.Components.RemindersList
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
        if (viewModel.screenState == TodoItemRemindersVM.ScreenState.VIEW_LIST) {
            RemindersList(reminders = reminders.value, onEvent = viewModel::onEvent)
        } else {

        }
    }
}