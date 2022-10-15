package com.example.todo.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.todo.Data.TodoDatabase
import com.example.todo.Data.TodoRepository
import com.example.todo.Data.TodoRepositoryImpl
import com.example.todo.Models.ReminderNotification
import com.example.todo.Models.Todo
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.CountDownLatch

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class TodoRepositoryTest {

    private lateinit var repository: TodoRepository
    private lateinit var database: TodoDatabase

    @Before
    fun setup() {
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                TodoDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        repository = TodoRepositoryImpl(database.todoDAO, database.reminderDAO)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun insertTodo(todo: Todo): Todo {

        val index = repository.getTodos().first().size

        val latch = CountDownLatch(1)
        withContext(Dispatchers.IO) {
            repository.insertTodoAsync(todo)
            latch.countDown()
        }

        latch.await()

        return repository.getTodos().first()[index]
    }

    @Test
    fun shouldInsertTodo() = runTest {
        val todo = Todo("foo", "foobody", id = 1)

        repository.insertTodoAsync(todo)

        val todos = repository.getTodos().first()

        assertEquals(listOf(todo), todos)
    }

    @Test
    fun shouldDeleteTodo() = runTest {
        val todo = Todo("foo", "Do the fooey", id = 1)

        repository.insertTodoAsync(todo)

        withContext(Dispatchers.IO) {
            val todos = repository.getTodos().first()
            assertEquals(true, todos.contains(todo))
        }

        withContext(Dispatchers.IO) {
            val reminders = repository.getAllRemindersByTodoId(todo.id!!).first()

            assertEquals(true, reminders.isEmpty())
        }
    }

    @Test
    fun shouldBeAbleToGetTodoById() = runTest {
        val todo = Todo("foo", "Do the fooey", id = 1)
        repository.insertTodoAsync(todo)

        val todoById = repository.getTodoByIDAsync(1)

        assertEquals(todoById, todo)
    }

    @Test
    fun shouldNotBeAbleToInsertReminderWithoutAssociatedTodo() = runTest {
        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = UUID.randomUUID().toString(),
            id = 1,
            todoId = 11,
            date = ZonedDateTime.now().toString()
        )

        withContext(Dispatchers.IO) {

            val result = repository.insertReminderAsync(reminder)
            assertEquals(false, result)
        }
        withContext(Dispatchers.IO) {
            val reminders = repository.getAllReminders().first()

            assertTrue(!reminders.contains(reminder))
        }
    }

    @Test
    fun shouldBeAbleToInsertReminder() = runTest {
        val todo = insertTodo(Todo("foo", "bar"))

        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = UUID.randomUUID().toString(),
            id = 1,
            todoId = todo.id!!,
            date = ZonedDateTime.now().toString()
        )
        withContext(Dispatchers.IO) { repository.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val reminders = repository.getAllReminders().first()

            assertTrue(reminders.contains(reminder))
        }
    }

    @Test
    fun shouldBeAbleToGetReminderById() = runTest {
        val todo = insertTodo(Todo("foo", "bar"))

        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = UUID.randomUUID().toString(),
            id = 1,
            todoId = todo.id!!,
            date = ZonedDateTime.now().toString()
        )
        withContext(Dispatchers.IO) { repository.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val storedReminder = repository.getReminderByIdAsync(1)

            assertEquals(reminder, storedReminder)
        }
    }

    @Test
    fun shouldBeAbleToGetReminderByUUID() = runTest {
        val todo = insertTodo(Todo("foo", "bar"))
        val uuid = UUID.randomUUID()
        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = uuid.toString(),
            id = 1,
            todoId = todo.id!!,
            date = ZonedDateTime.now().toString()
        )
        withContext(Dispatchers.IO) { repository.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val storedReminder = repository.getReminderByUUIDAsync(uuid)

            assertEquals(reminder, storedReminder)
        }
    }

    @Test
    fun shouldBeAbleToGetReminderByStringUUID() = runTest {
        val todo = insertTodo(Todo("foo", "bar"))
        val uuid = UUID.randomUUID()
        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = uuid.toString(),
            id = 1,
            todoId = todo.id!!,
            date = ZonedDateTime.now().toString()
        )
        withContext(Dispatchers.IO) { repository.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val storedReminder = repository.getReminderByUUIDAsync(uuid.toString())

            assertEquals(reminder, storedReminder)
        }
    }
}