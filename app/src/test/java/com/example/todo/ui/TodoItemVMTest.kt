package com.example.todo.ui

import androidx.lifecycle.SavedStateHandle
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.Todo
import com.example.todo.Util.Routes
import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoItem.TodoItemEvent
import com.example.todo.ui.TodoItem.TodoItemVM
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
class TodoItemVMTest {

    private lateinit var repository: TodoRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = mock {}
        savedStateHandle = mock {}

        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Make sure the view model launches in the correct state with
     * the provided todoitem
     */
    private suspend fun launchWithTodo(todo: Todo) {
        whenever(savedStateHandle.get<Int>(any()))
            .thenReturn(todo.id!!)
        whenever(repository.getTodoByIDAsync(eq(todo.id!!)))
            .thenReturn(todo)
    }

    @Test
    fun `Should start in view state on recieved todo`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)

        assertEquals(TodoItemVM.TodoState.VIEW, viewModel.state)
    }

    @Test
    fun `Should start in edit state on no recieved todo`() = runTest {
        val todo = Todo("foo", "foo", id = -1)

        val viewModel = TodoItemVM(repository, savedStateHandle)

        assertEquals(TodoItemVM.TodoState.EDIT, viewModel.state)
    }

    @Test
    fun `Should change state to view if recieve backpress event while in edit state`() = runTest {
        val todo = Todo("foo", "foo", id = -1)

        val viewModel = TodoItemVM(repository, savedStateHandle)

        assertEquals(TodoItemVM.TodoState.EDIT, viewModel.state)

        viewModel.onEvent(TodoItemEvent.BackPress)

        assertEquals(TodoItemVM.TodoState.VIEW, viewModel.state)
    }

    @Test
    fun `Should send popStackBack event if recieving backpress event if in view state`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)

        assertEquals(TodoItemVM.TodoState.VIEW, viewModel.state)

        val UIEvents = viewModel.uiEvents

        viewModel.onEvent(TodoItemEvent.BackPress)

        assertEquals(UIEvent.popStackBack, UIEvents.first())
    }

    @Test
    fun `Should set title on setTitleEvent`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)
        withContext(Dispatchers.Default) { delay(2) }

        assertEquals(TodoItemVM.TodoState.VIEW, viewModel.state)
        assertEquals("foo", viewModel.title)

        viewModel.onEvent(TodoItemEvent.setTitle("bar"))

        assertEquals("bar", viewModel.title)
    }

    @Test
    fun `Should set body on setBodyEvent`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)
        withContext(Dispatchers.Default) { delay(2) }

        assertEquals(TodoItemVM.TodoState.VIEW, viewModel.state)
        assertEquals("foo", viewModel.body)

        viewModel.onEvent(TodoItemEvent.setBody("bar"))

        assertEquals("bar", viewModel.body)
    }

    @Test
    fun `Should set state to value of setEditStateEvent`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)

        assertEquals(TodoItemVM.TodoState.VIEW, viewModel.state)

        viewModel.onEvent(TodoItemEvent.setEditState(TodoItemVM.TodoState.EDIT))

        assertEquals(TodoItemVM.TodoState.EDIT, viewModel.state)

        viewModel.onEvent(TodoItemEvent.setEditState(TodoItemVM.TodoState.VIEW))

        assertEquals(TodoItemVM.TodoState.VIEW, viewModel.state)
    }

    @Test
    fun `Should invoke insertTodoAsync on saveTodoEvent`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)
        withContext(Dispatchers.Default) { delay(2) }

        viewModel.onEvent(TodoItemEvent.saveTodo)

        withContext(Dispatchers.Default) { delay(2) }

        verify(repository, atLeastOnce()).insertTodoAsync(eq(todo))
    }

    @Test
    fun `Should generate popStackBack event on saveTodoEvent`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)

        val UIEvents = viewModel.uiEvents

        viewModel.onEvent(TodoItemEvent.saveTodo)

        withContext(Dispatchers.Default) { delay(2) }

        assertEquals(UIEvent.popStackBack, UIEvents.first())
    }

    @Test
    fun `Should generate navigate event on NavigateToRemindersEvent`() = runTest {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemVM(repository, savedStateHandle)
        withContext(Dispatchers.Default) { delay(2) }

        val UIEvents = viewModel.uiEvents

        viewModel.onEvent(TodoItemEvent.navigateToReminders)

        withContext(Dispatchers.Default) { delay(2) }

        val event = UIEvents.first()
        assertTrue(event is UIEvent.navigate)
        assertEquals(Routes.TODO_REMINDER+ "?todoId=${todo!!.id!!}",(event as UIEvent.navigate).route )
    }
}