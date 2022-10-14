package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.todo.Util.Routes
import com.example.todo.Views.TodoItemScreen
import com.example.todo.Views.TodoListScreen
import com.example.todo.ui.TodoItemReminders.TodoItemRemindersScreen
import com.example.todo.ui.theme.TodoTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            TodoTheme {
                NavHost(navController = navController , startDestination = Routes.TODO_LIST ) {
                    composable(Routes.TODO_LIST) { TodoListScreen { navController.navigate(it.route) } }
                    composable(
                        route = Routes.TODO_ITEM + "?todoId={todo_id}",
                        arguments = listOf(
                            navArgument("todo_id") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) { TodoItemScreen { navController.popBackStack() } }
                    composable(
                        route = Routes.TODO_REMINDER + "?todoId={todo_id}",
                        arguments = listOf(
                            navArgument("todo_id") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) { TodoItemRemindersScreen { navController.popBackStack() } }
                }
            }
        }
    }
}