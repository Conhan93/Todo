package com.example.todo.ui

import com.example.todo.Data.TodoRepository
import com.example.todo.Models.Services.TodoNotificationService
import com.example.todo.Models.Todo
import com.example.todo.Util.Routes
import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoList.TodoListUIEvent
import com.example.todo.ui.TodoList.TodoListVM
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListVMTest {

    private lateinit var repository: TodoRepository
    private lateinit var notificationService: TodoNotificationService
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = mock {}
        notificationService = mock {}

        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Filter state should be set to all on start`() = runTest {
        val viewModel = TodoListVM(repository, notificationService)

        assertEquals(TodoListVM.TodoFilter.ALL, viewModel.filterState)
    }

    @Test
    fun `FilterTodos event should set filterState`() {
        val viewModel = TodoListVM(repository, notificationService)

        assertEquals(TodoListVM.TodoFilter.ALL, viewModel.filterState)

        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.NOT_DONE))
        assertEquals(TodoListVM.TodoFilter.NOT_DONE, viewModel.filterState)

        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.DONE))
        assertEquals(TodoListVM.TodoFilter.DONE, viewModel.filterState)
    }

    @Test
    fun `Should set filterState to all if receiving filter value equal to filter state`() {
        val viewModel = TodoListVM(repository, notificationService)

        assertEquals(TodoListVM.TodoFilter.ALL, viewModel.filterState)

        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.NOT_DONE))
        assertEquals(TodoListVM.TodoFilter.NOT_DONE, viewModel.filterState)
        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.NOT_DONE))
        assertEquals(TodoListVM.TodoFilter.ALL, viewModel.filterState)

        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.DONE))
        assertEquals(TodoListVM.TodoFilter.DONE, viewModel.filterState)
        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.DONE))
        assertEquals(TodoListVM.TodoFilter.ALL, viewModel.filterState)
    }

    @Test
    fun `Should generate navigate event with id on selectItemEvent`() = runTest {
        val id = 1
        val viewModel = TodoListVM(repository, notificationService)

        val UIEvents = viewModel.uiEvents

        viewModel.onUIEvent(TodoListUIEvent.itemSelect(id))

        withContext(Dispatchers.Default) { delay(2) }

        val event = UIEvents.first()
        assertTrue(event is UIEvent.navigate)
        assertEquals(Routes.TODO_ITEM + "?todoId=${id}", (event as UIEvent.navigate).route)
    }

    @Test
    fun `Should generate navigate event without id on addItemEvent`() = runTest {
        val viewModel = TodoListVM(repository, notificationService)

        val UIEvents = viewModel.uiEvents

        viewModel.onUIEvent(TodoListUIEvent.addItem)

        val event = UIEvents.first()
        assertTrue(event is UIEvent.navigate)
        assertEquals(Routes.TODO_ITEM, (event as UIEvent.navigate).route)
    }

    @Test
    fun `Should check todo on itemCheckEvent`() = runTest {
        val id = 1
        val todo = Todo("foo", "foo", id = id)
        val checkedTodo = todo.copy(isCompleted = true)

        whenever(repository.getTodoByIDAsync(id))
            .thenReturn(todo)
        whenever(repository.getTodos())
            .thenReturn(flowOf(listOf(todo)))

        val viewModel = TodoListVM(repository, notificationService)
        val todos = viewModel.todos.first()

        assertTrue(todos.contains(todo))

        viewModel.onUIEvent(TodoListUIEvent.itemCheck(id, check = true))

        withContext(Dispatchers.Default) { delay(2) }

        verify(repository, atLeastOnce()).insertTodoAsync(eq(checkedTodo))
    }

    @Test
    fun `Should filter todos based on filterState`() = runTest {
        val todoChecked = Todo("foo", "bar", id = 1, isCompleted = true)
        val todoUnchecked =  Todo("bar", "foo", id = 2, isCompleted = false)

        val todosAll = listOf(todoChecked, todoUnchecked)
        val todosDone = listOf(todoChecked)
        val todosNotDone = listOf(todoUnchecked)

        whenever(repository.getTodos())
            .thenReturn(flowOf(todosAll))

        val viewModel = TodoListVM(repository, notificationService)

        var todos = viewModel.todos

        assertEquals(todosAll, todos.first())

        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.DONE))
        withContext(Dispatchers.Default) { delay(2) }

        todos = viewModel.todos
        assertEquals(todosDone, todos.first())

        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.NOT_DONE))
        withContext(Dispatchers.Default) { delay(2) }

        todos = viewModel.todos
        assertEquals(todosNotDone, todos.first())

        viewModel.onUIEvent(TodoListUIEvent.filterTodos(TodoListVM.TodoFilter.NOT_DONE))
        withContext(Dispatchers.Default) { delay(2) }

        todos = viewModel.todos
        assertEquals(todosAll, todos.first())

    }

    @Test
    fun `Should generate show snack bar UI event on deleteEvent`() = runTest {
        val id = 1
        val todo = Todo("foo", "foo", id = id)

        whenever(repository.getTodos())
            .thenReturn(flowOf(listOf(todo)))

        val viewModel = TodoListVM(repository, notificationService)

        val UIEvents = viewModel.uiEvents

        viewModel.onUIEvent(TodoListUIEvent.itemDelete(id))

        val expectedEvent = UIEvent.ShowSnackBar("Todo deleted", "Undo")
        assertEquals(expectedEvent, UIEvents.first())
    }

    @Test
    fun `Should restore deleted todo on UndoDeleteTodo`() = runTest {
        val id = 1
        val todo = Todo("foo", "foo", id = id)

        val savedTodos = mutableListOf(todo)

        whenever(repository.getTodos())
            .thenReturn(flowOf(savedTodos))
        whenever(repository.getTodoByIDAsync(eq(id)))
            .thenReturn(savedTodos.first { it.id == id })
        whenever(repository.insertTodoAsync(eq(todo)))
            .then { savedTodos.add(todo) }
        whenever(repository.deleteTodoAsync(eq(todo)))
            .then { savedTodos.removeFirst() }

        whenever(repository.getAllRemindersByTodoId(eq(id)))
            .thenReturn(flowOf(listOf()))

        val viewModel = TodoListVM(repository, notificationService)

        assertEquals(listOf(todo), viewModel.todos.first())

        viewModel.onUIEvent(TodoListUIEvent.itemDelete(id))

        withContext(Dispatchers.IO) { delay(2) }

        verify(repository, atLeastOnce()).deleteTodoAsync(eq(todo))
        assertTrue(viewModel.todos.first().isEmpty())

        viewModel.onUIEvent(TodoListUIEvent.UndoDeleteTodo)

        withContext(Dispatchers.IO) { delay(2) }

        assertEquals(listOf(todo), viewModel.todos.first())
    }
}