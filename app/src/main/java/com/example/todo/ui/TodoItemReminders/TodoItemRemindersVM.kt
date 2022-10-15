package com.example.todo.ui.TodoItemReminders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo
import com.example.todo.Util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemRemindersVM @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private lateinit var todo: Todo
    private val todoId = savedStateHandle.get<Int>("todo_id") ?: -1

    val reminders = repository.getAllRemindersByTodoId(todoId)

    enum class ScreenState { VIEW, EDIT, ADD }
    var screenState by mutableStateOf(ScreenState.VIEW)
        private set


    private val _uiEvents = Channel<UIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getTodoByIDAsync(todoId)?.let {
                todo = it
            }
        }
    }

    fun onEvent(event: TodoItemRemindersEvent) {
        when(event) {
            TodoItemRemindersEvent.BackPressEvent -> {
                viewModelScope.launch() {
                    _uiEvents.send(UIEvent.popStackBack)
                }
            }
            TodoItemRemindersEvent.SaveEvent -> TODO()
            is TodoItemRemindersEvent.AddEvent -> addReminder(event.reminder)
            is TodoItemRemindersEvent.DeleteEvent -> deleteReminder(event.reminder)
            is TodoItemRemindersEvent.EditEvent -> TODO()
        }

    }

    private fun addReminder(reminderNotification: ReminderNotification) {
        viewModelScope.launch {
            repository.insertReminderAsync(reminderNotification)
        }
    }

    private fun deleteReminder(reminderNotification: ReminderNotification) {
        viewModelScope.launch {
            repository.deleteReminderAsync(reminderNotification)
        }
    }
}