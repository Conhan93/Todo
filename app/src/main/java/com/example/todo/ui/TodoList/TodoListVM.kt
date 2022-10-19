package com.example.todo.ui.TodoList


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.Services.TodoNotificationService
import com.example.todo.Models.Todo
import com.example.todo.Util.Routes
import com.example.todo.Util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoListVM @Inject constructor(
    private val repository : TodoRepository,
    private val notificationService: TodoNotificationService
) : ViewModel() {

    enum class TodoFilter { DONE, NOT_DONE, ALL }
    var filterState by mutableStateOf(TodoFilter.ALL)
        private set

    var todos = repository.getTodos()

    private val _uiEvents = Channel<UIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    private lateinit var latestDeletedTodo: Todo

    fun onUIEvent(event : TodoListUIEvent) {
        when(event) {
            is TodoListUIEvent.itemDelete -> {
                deleteTodo(event.id)
                sendUIEvent(UIEvent.ShowSnackBar(
                    message = "Todo deleted",
                    actionLabel = "Undo"
                ))
            }
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
            is TodoListUIEvent.filterTodos -> {
                filterState = if (event.filter == filterState) TodoFilter.ALL else event.filter

                todos = updateTodos(filterState)
            }
            TodoListUIEvent.UndoDeleteTodo -> {
                viewModelScope.launch {
                    repository.insertTodoAsync(latestDeletedTodo)
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
                latestDeletedTodo = todo

                withContext(Dispatchers.IO) {
                    val reminders = repository.getAllRemindersByTodoId(todo.id!!).first()

                    reminders.forEach { notificationService.removeNotification(it) }
                }
                repository.deleteTodoAsync(todo)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun updateTodos(filter: TodoFilter): Flow<List<Todo>> {
        return repository
            .getTodos()
            .mapLatest { todos ->
                when(filter) {
                    TodoFilter.DONE -> todos.filter { it.isCompleted }
                    TodoFilter.NOT_DONE -> todos.filter { it.isCompleted.not() }
                    TodoFilter.ALL -> todos
                }
            }
    }
}