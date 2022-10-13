package com.example.todo.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.todo.Data.ReminderDAO
import com.example.todo.Data.TodoDatabase
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class ReminderDaoTest {

    private lateinit var database: TodoDatabase
    private lateinit var dao: ReminderDAO

    val dummyTodoId = 1

    @Before
    fun setup() {
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                TodoDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        dao = database.reminderDAO

    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun insertDummyTodo() {
        val todoDao = database.todoDAO
        val todo = Todo("foo", "boo", id = dummyTodoId)

        todoDao.insertTodoAsync(todo)
    }

    @Test
    fun shouldBeAbleToInsertReminder() = runTest {

        withContext(Dispatchers.IO) { insertDummyTodo() }

        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = UUID.randomUUID().toString(),
            id = 1,
            todoId = dummyTodoId,
            date = ZonedDateTime.now().toString()
        )

        withContext(Dispatchers.IO) { dao.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val reminders = dao.getAllReminders().first()

            assertTrue(reminders.contains(reminder))
        }
    }

    @Test
    fun deleteShouldRemoveTodo() = runTest {
        withContext(Dispatchers.IO) { insertDummyTodo() }

        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = UUID.randomUUID().toString(),
            id = 1,
            todoId = dummyTodoId,
            date = ZonedDateTime.now().toString()
        )

        withContext(Dispatchers.IO) { dao.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val reminders = dao.getAllReminders().first()

            assert(reminders.contains(reminder))
        }

        withContext(Dispatchers.IO) { dao.deleteReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val reminders = dao.getAllReminders().first()

            assert(!reminders.contains(reminder))
        }
    }

    @Test
    fun shouldBeAbleToGetReminderById() = runTest {
        withContext(Dispatchers.IO) { insertDummyTodo() }

        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = UUID.randomUUID().toString(),
            id = 1,
            todoId = dummyTodoId,
            date = ZonedDateTime.now().toString()
        )

        withContext(Dispatchers.IO) { dao.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val actualReminder = dao.getReminderByIdAsync(1)

            assertEquals(actualReminder, reminder)
        }
    }

    @Test
    fun shouldBeAbleToGetReminderByUUID() = runTest {
        withContext(Dispatchers.IO) { insertDummyTodo() }

        val uuid = UUID.randomUUID()

        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = uuid.toString(),
            id = 1,
            todoId = dummyTodoId,
            date = ZonedDateTime.now().toString()
        )

        withContext(Dispatchers.IO) { dao.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val actualReminder = dao.getReminderByUUIDAsync(uuid)

            assertEquals(actualReminder, reminder)
        }
    }

    @Test
    fun shouldBeAbleToGetReminderByStringUUID() = runTest {
        withContext(Dispatchers.IO) { insertDummyTodo() }

        val uuid = UUID.randomUUID().toString()

        val reminder = ReminderNotification(
            title = "foo",
            message = "boo",
            uuid = uuid,
            id = 1,
            todoId = dummyTodoId,
            date = ZonedDateTime.now().toString()
        )

        withContext(Dispatchers.IO) { dao.insertReminderAsync(reminder) }
        withContext(Dispatchers.IO) {
            val actualReminder = dao.getReminderByUUIDAsync(uuid)

            assertEquals(actualReminder, reminder)
        }
    }

    @Test
    fun shouldGetAllReminders() = runTest {
        withContext(Dispatchers.IO) { insertDummyTodo() }

        val reminders = listOf(
            ReminderNotification(
                title = "foo0",
                message = "boo0",
                uuid = UUID.randomUUID().toString(),
                id = 1,
                todoId = dummyTodoId,
                date = ZonedDateTime.now().toString()
            ),
            ReminderNotification(
                title = "foo1",
                message = "boo1",
                uuid = UUID.randomUUID().toString(),
                id = 2,
                todoId = dummyTodoId,
                date = ZonedDateTime.now().toString()
            ),
            ReminderNotification(
                title = "foo2",
                message = "boo2",
                uuid = UUID.randomUUID().toString(),
                id = 3,
                todoId = dummyTodoId,
                date = ZonedDateTime.now().toString()
            )
        )

        withContext(Dispatchers.IO) { reminders.forEach { dao.insertReminderAsync(it)} }

        assertEquals(reminders,dao.getAllReminders().first())

    }

    @Test
    fun shouldGetAllRemindersForTodo() = runTest {
        withContext(Dispatchers.IO) { insertDummyTodo() }

        val reminders = listOf(
            ReminderNotification(
                title = "foo0",
                message = "boo0",
                uuid = UUID.randomUUID().toString(),
                id = 1,
                todoId = dummyTodoId,
                date = ZonedDateTime.now().toString()
            ),
            ReminderNotification(
                title = "foo1",
                message = "boo1",
                uuid = UUID.randomUUID().toString(),
                id = 2,
                todoId = dummyTodoId,
                date = ZonedDateTime.now().toString()
            ),
            ReminderNotification(
                title = "foo2",
                message = "boo2",
                uuid = UUID.randomUUID().toString(),
                id = 3,
                todoId = dummyTodoId,
                date = ZonedDateTime.now().toString()
            )
        )

        withContext(Dispatchers.IO) { reminders.forEach { dao.insertReminderAsync(it)} }

        assertEquals(reminders,dao.getAllRemindersByTodoId(dummyTodoId).first())
    }
}