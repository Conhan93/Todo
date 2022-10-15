package com.example.todo.ui.TodoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.Services.TodoNotificationService
import com.example.todo.Util.Routes
import com.example.todo.Util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoListVM @Inject constructor(
    private val repository : TodoRepository,
    private val notificationService: TodoNotificationService
) : ViewModel() {

    val todos = repository.getTodos()

    private val _uiEvents = Channel<UIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onUIEvent(event : TodoListUIEvent) {
        when(event) {
            is TodoListUIEvent.itemDelete -> deleteTodo(event.id)
            is TodoListUIEvent.itemSelect -> sendUIEvent(UIEvent.navigate(Routes.TODO_ITEM + "?todoId=${event.id}"))
            TodoListUIEvent.addItem -> sendUIEvent(UIEvent.navigate(Routes.TODO_ITEM))
            is TodoListUIEvent.itemCheck -> {
                viewModelScope.launch {
                    repository.getTodoByIDAsync(event.id)?.let { todo ->
                        val checkedTodo = todo.copy(isCompleted = event.check)
                        repository.insertTodoAsync(checkedTodo)
                    }
                }
            }
        }
    }

    private fun sendUIEvent(event : UIEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }

    private fun deleteTodo(id : Int) {
        viewModelScope.launch {
            repository.getTodoByIDAsync(id)?.let { todo ->
                withContext(Dispatchers.IO) {
                    val reminders = repository.getAllRemindersByTodoId(todo.id!!).first()

                    reminders.forEach { notificationService.removeNotification(it) }
                }
                repository.deleteTodoAsync(todo)
            }
        }
    }
}