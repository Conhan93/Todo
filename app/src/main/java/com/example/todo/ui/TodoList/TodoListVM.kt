package com.example.todo.ui.TodoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.Data.TodoRepository
import com.example.todo.Util.Routes
import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoList.TodoListUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListVM @Inject constructor(
    private val repository : TodoRepository
) : ViewModel() {

    val todos = repository.getTodos()

    private val _uiEvents = Channel<UIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onUIEvent(event : TodoListUIEvent) {
        when(event) {
            is TodoListUIEvent.itemDelete -> TODO()
            is TodoListUIEvent.itemSelect -> sendUIEvent(UIEvent.navigate(Routes.TODO_ITEM + "?todoId=${event.id}"))
            TodoListUIEvent.addItem -> sendUIEvent(UIEvent.navigate(Routes.TODO_ITEM))
        }
    }

    private fun sendUIEvent(event : UIEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }
}