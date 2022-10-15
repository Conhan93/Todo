package com.example.todo.ui.TodoItemReminders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Services.TodoNotificationService
import com.example.todo.Models.Todo
import com.example.todo.Util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodoItemRemindersVM @Inject constructor(
    private val repository: TodoRepository,
    private val notificationService: TodoNotificationService,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private lateinit var todo: Todo
    private val todoId = savedStateHandle.get<Int>("todo_id") ?: -1

    val reminders = repository.getAllRemindersByTodoId(todoId)

    enum class ScreenState { VIEW_LIST, ADD }
    var screenState by mutableStateOf(ScreenState.VIEW_LIST)
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
                if (screenState != ScreenState.VIEW_LIST) {
                    screenState = ScreenState.VIEW_LIST
                } else {
                    viewModelScope.launch() {
                        _uiEvents.send(UIEvent.popStackBack)
                    }
                }
            }
            TodoItemRemindersEvent.SaveEvent -> TODO()
            TodoItemRemindersEvent.NewReminderEvent -> {
                screenState = ScreenState.ADD
            }
            is TodoItemRemindersEvent.DeleteEvent -> deleteReminder(event.reminder)
            is TodoItemRemindersEvent.AddEvent -> {
                addReminder(event.dateTime)

                screenState = ScreenState.VIEW_LIST
            }
        }

    }

    private fun addReminder(dateTime: ZonedDateTime) {

        val reminder = ReminderNotification(
            title = todo.title,
            message = todo.body,
            uuid = UUID.randomUUID().toString(),
            todoId = todoId,
            date = dateTime.toString()
        )

        viewModelScope.launch {
            repository.insertReminderAsync(reminder)
            notificationService.scheduleNotification(
                todo,
                dateTime
            )
        }
    }

    private fun deleteReminder(reminderNotification: ReminderNotification) {
        viewModelScope.launch {
            repository.deleteReminderAsync(reminderNotification)
        }
    }
}