package com.example.todo.ui.TodoItemReminders


import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.Util.UIEvent
import com.example.todo.ui.Components.DateTimePicker
import com.example.todo.ui.TodoItem.TodoItemVM
import com.example.todo.ui.TodoItemReminders.Components.NewReminderButton
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
        topBar = { TodoItemRemindersTopBar(onEvent = viewModel::onEvent)},
        floatingActionButton = {
            if (viewModel.screenState == TodoItemRemindersVM.ScreenState.VIEW_LIST)
                NewReminderButton(onEvent = viewModel::onEvent)
        }
    ) {
        when(viewModel.screenState) {
            TodoItemRemindersVM.ScreenState.VIEW_LIST -> {
                RemindersList(reminders = reminders.value, onEvent = viewModel::onEvent)
            }
            TodoItemRemindersVM.ScreenState.ADD -> {
                DateTimePicker {
                    viewModel.onEvent(TodoItemRemindersEvent.AddEvent(it))
                }
            }
        }
    }
}