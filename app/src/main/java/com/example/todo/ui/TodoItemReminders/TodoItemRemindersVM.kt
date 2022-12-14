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
    private lateinit var lastDeletedReminder: ReminderNotification

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
            TodoItemRemindersEvent.NewReminderEvent -> {
                screenState = ScreenState.ADD
            }
            is TodoItemRemindersEvent.DeleteEvent -> {
                deleteReminder(event.reminder)

                viewModelScope.launch {
                    _uiEvents.send(UIEvent.ShowSnackBar(
                        "Reminder deleted",
                        "Undo"
                    ))
                }
            }
            is TodoItemRemindersEvent.AddEvent -> {
                addReminder(event.dateTime)

                screenState = ScreenState.VIEW_LIST
            }
            TodoItemRemindersEvent.UndoDelete -> {
                val dateTime = ZonedDateTime.parse(lastDeletedReminder.date)
                addReminder(dateTime)
            }
        }

    }

    private fun addReminder(dateTime: ZonedDateTime) {

        viewModelScope.launch {
            notificationService.scheduleNotification(todo, dateTime)
        }
    }

    private fun deleteReminder(reminderNotification: ReminderNotification) {
        lastDeletedReminder = reminderNotification

        viewModelScope.launch {
            notificationService.removeNotification(reminderNotification)
        }
    }
}