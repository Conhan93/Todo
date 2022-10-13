package com.example.todo.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.todo.Data.TodoDAO
import com.example.todo.Data.TodoDatabase
import com.example.todo.Models.Todo
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class TodoDaoTest {

    private lateinit var database: TodoDatabase
    private lateinit var dao: TodoDAO

    @Before
    fun setupDatabase() {
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                TodoDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        dao = database.todoDAO
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun shouldGetAllTodos() = runTest {
        val todos = listOf(
            Todo("foo","", id = 1),
            Todo("bar","", id = 2),
            Todo("Har","", id = 3)
        )

        todos.forEach { dao.insertTodoAsync(it) }

        val actualTodos = dao.getTodos().first()

        assertEquals(todos, actualTodos)
    }

    @Test
    fun insertShouldInsertTodo() = runTest {
        val todo = Todo("foo", "Do the fooey", id = 1)
        dao.insertTodoAsync(todo)


        withContext(Dispatchers.IO) {
            val todos = dao.getTodos().first()
            assert(todos.contains(todo))
        }


    }

    @Test
    fun deleteShouldRemoveTodo() = runTest {
        val todo = Todo("foo", "Do the fooey", id = 1)
        dao.insertTodoAsync(todo)


        withContext(Dispatchers.IO) {
            val todos = dao.getTodos().first()
            assert(todos.contains(todo))
        }

        dao.deleteTodoAsync(todo)

        withContext(Dispatchers.IO) {
            val todos = dao.getTodos().first()
            assert(!todos.contains(todo))
        }
    }

    @Test
    fun shouldBeAbleToGetTodoById() = runTest {
        val todo = Todo("foo", "Do the fooey", id = 1)
        dao.insertTodoAsync(todo)

        val todoById = dao.getTodoByIDAsync(1)

        assertEquals(todoById, todo)
    }
}