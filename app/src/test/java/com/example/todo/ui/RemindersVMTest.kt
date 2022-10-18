package com.example.todo.ui

import androidx.lifecycle.SavedStateHandle
import com.example.todo.Data.TodoRepository
import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Services.TodoNotificationService
import com.example.todo.Models.Todo
import com.example.todo.Util.UIEvent
import com.example.todo.ui.TodoItemReminders.TodoItemRemindersEvent
import com.example.todo.ui.TodoItemReminders.TodoItemRemindersVM
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.time.ZonedDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class RemindersVMTest {

    private lateinit var repository: TodoRepository
    private lateinit var notificationService: TodoNotificationService
    private lateinit var savedStateHandle: SavedStateHandle

    private val dispatcher = StandardTestDispatcher()

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Before
    fun setup() {
        repository = mock {}
        notificationService = mock {}
        savedStateHandle = mock {}

        Dispatchers.setMain(dispatcher)
    }

    /**
     * Make sure the view model launches in the correct state with
     * the provided todoitem
     */
    private suspend fun launchWithTodo(todo: Todo) {
        whenever(savedStateHandle.get<Int>(any()))
            .thenReturn(todo.id!!)
        whenever(repository.getTodoByIDAsync(todo.id!!))
            .thenReturn(todo)
    }

    private suspend fun prepareVMForSimpleStateTest(): TodoItemRemindersVM {
        val todo = Todo("foo", "foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemRemindersVM(
            repository = repository,
            notificationService = notificationService,
            savedStateHandle = savedStateHandle
        )

        return viewModel
    }


    @Test
    fun `Should start with passed todo id`() = runTest {
        val passedId = 10
        val todo = Todo("foo", "foo", id = passedId)
        val reminder = ReminderNotification(
            todo.title,
            todo.body,
            UUID.randomUUID().toString(),
            todoId = todo.id!!,
            date = ZonedDateTime.now().toString()
        )
        val associatedReminders = flowOf(listOf(reminder))

        whenever(savedStateHandle.get<Int>(any()))
            .thenReturn(passedId)
        whenever(repository.getAllRemindersByTodoId(passedId))
            .thenReturn(associatedReminders)
        whenever(repository.getTodoByIDAsync(passedId))
            .thenReturn(todo)

        val viewModel = TodoItemRemindersVM(
            repository = repository,
            notificationService = notificationService,
            savedStateHandle = savedStateHandle
        )

        val expected = associatedReminders.first()
        val actual = viewModel.reminders.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `Should start with screen state as view list`() = runTest {
        val viewModel = prepareVMForSimpleStateTest()

        assertEquals(TodoItemRemindersVM.ScreenState.VIEW_LIST, viewModel.screenState)
    }

    @Test
    fun `Should switch screen state to add on new reminder event`() = runTest {
        val viewModel = prepareVMForSimpleStateTest()

        assertEquals(TodoItemRemindersVM.ScreenState.VIEW_LIST, viewModel.screenState)

        viewModel.onEvent(TodoItemRemindersEvent.NewReminderEvent)

        assertEquals(TodoItemRemindersVM.ScreenState.ADD, viewModel.screenState)
    }

    @Test
    fun `Should switch to screen state view list on passed AddEvent`() = runTest {
        val viewModel = prepareVMForSimpleStateTest()

        assertEquals(TodoItemRemindersVM.ScreenState.VIEW_LIST, viewModel.screenState)

        viewModel.onEvent(TodoItemRemindersEvent.NewReminderEvent)

        assertEquals(TodoItemRemindersVM.ScreenState.ADD, viewModel.screenState)

        viewModel.onEvent(TodoItemRemindersEvent.AddEvent(ZonedDateTime.now()))

        assertEquals(TodoItemRemindersVM.ScreenState.VIEW_LIST, viewModel.screenState)
    }

    @Test
    fun `Should switch back to view list from add on backpress event`() = runTest {
        val viewModel = prepareVMForSimpleStateTest()

        assertEquals(TodoItemRemindersVM.ScreenState.VIEW_LIST, viewModel.screenState)

        viewModel.onEvent(TodoItemRemindersEvent.NewReminderEvent)

        assertEquals(TodoItemRemindersVM.ScreenState.ADD, viewModel.screenState)

        viewModel.onEvent(TodoItemRemindersEvent.BackPressEvent)

        assertEquals(TodoItemRemindersVM.ScreenState.VIEW_LIST, viewModel.screenState)
    }

    @Test
    fun `Should generate a popStackBack UI event on backpress on view list state`() = runTest {
        val viewModel = prepareVMForSimpleStateTest()

        assertEquals(TodoItemRemindersVM.ScreenState.VIEW_LIST, viewModel.screenState)

        val UIEvents = viewModel.uiEvents

        viewModel.onEvent(TodoItemRemindersEvent.BackPressEvent)

        assertEquals(UIEvent.popStackBack, UIEvents.first())
    }

    @Test
    fun `Add reminder event should schedule notification`() = runTest {
        val todo = Todo("foo","foo", id = 1)
        launchWithTodo(todo)

        val viewModel = TodoItemRemindersVM(
            repository = repository,
            notificationService = notificationService,
            savedStateHandle = savedStateHandle
        )

        viewModel.onEvent(TodoItemRemindersEvent.AddEvent(ZonedDateTime.now()))

        withContext(Dispatchers.IO) { delay(5) }

        verify(notificationService, atLeastOnce()).scheduleNotification(eq(todo), any())
    }

    @Test
    fun `Remove reminder should remove notification`() = runTest {
        val todo = Todo("foo","foo", id = 1)
        val reminder = ReminderNotification(
            todo.title,
            todo.body,
            UUID.randomUUID().toString(),
            todoId = todo.id!!,
            date = ZonedDateTime.now().toString()
        )
        launchWithTodo(todo)

        val viewModel = TodoItemRemindersVM(
            repository = repository,
            notificationService = notificationService,
            savedStateHandle = savedStateHandle
        )

        viewModel.onEvent(TodoItemRemindersEvent.DeleteEvent(reminder))

        withContext(Dispatchers.IO) { delay(5) }

        verify(notificationService, atLeastOnce()).removeNotification(eq(reminder))
    }
}