package com.example.todo.ui.TodoItemReminders

import androidx.lifecycle.ViewModel
import com.example.todo.Util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class TodoItemRemindersVM: ViewModel() {

    private val _uiEvents = Channel<UIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onEvent(event: TodoItemRemindersEvent) {
        when(event) {
            TodoItemRemindersEvent.backPressEvent -> {
                viewModelScope.launch() {
                    _uiEvents.send(UIEvent.popStackBack)
                }
            }
            TodoItemRemindersEvent.saveEvent -> TODO()
        }
    }
}