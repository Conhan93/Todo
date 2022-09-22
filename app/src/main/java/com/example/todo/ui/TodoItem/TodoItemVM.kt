package com.example.todo.ui.TodoItem

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.Todo
import com.example.todo.Util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemVM @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set
    var _title by mutableStateOf("")
        private set
    var _body by mutableStateOf("")
        private set

    private val _uiEvents = Channel<UIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()


    init {
        val id = savedStateHandle.get<Int>("todo_id")

        if(id != null && id != -1)
            viewModelScope.launch {
                repository.getTodoByIDAsync(id)?.let {
                    _title = it.title
                    _body = it.body
                    todo = it
                }
            }
        else
            todo = Todo("", "")
    }

    fun onEvent(event: TodoItemEvent) {
        when(event) {
            TodoItemEvent.saveTodo -> saveTodo()
            is TodoItemEvent.setBody -> _body = event.body
            is TodoItemEvent.setTitle -> _title = event.title
        }
    }

    private fun saveTodo() {
        todo?.let {
            it.title = _title
            it.body = _body

            viewModelScope.launch {
                repository.insertTodoAsync(it)
            }
        }
        Log.d("TodoItemVM", "saved todo : $todo")

        sendUIEvent(UIEvent.popStackBack)

    }

    private fun sendUIEvent(event : UIEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }
}