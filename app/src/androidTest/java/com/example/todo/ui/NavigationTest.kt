package com.example.todo.ui

import android.content.res.Resources
import androidx.compose.ui.test.*
import com.example.todo.R

import androidx.compose.ui.test.junit4.createAndroidComposeRule

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.todo.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)
    lateinit var getString: (Int) -> String
    @Before
    fun setup() {
        getString = composeTestRule.activity::getString
    }

    @Test
    fun startScreenIsTodoListScreen() {

        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()

        composeTestRule
            .onNodeWithText(getString(R.string.todo_filter_button_not_done))
            .assertExists()
        composeTestRule
            .onNodeWithText(getString(R.string.todo_filter_button_done))
            .assertExists()
        composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_list_add_button))
            .assertExists()
    }

    @Test
    fun navigateToAddNewTodoScreen() {
        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()

        val addButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_add_button))
        addButton.performClick()

        composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_body_box))
            .assertExists()
        composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_title_box))
            .assertExists()
        composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_item_save_button))
            .assertExists()
        var backButton = composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_item_back_button))

        backButton.assertExists()
        backButton.performClick()

        backButton = composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_item_back_button))

        backButton.assertExists()
        backButton.performClick()

        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()
    }

    @Test
    fun navigateToTodoItemScreen() {
        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()

        val addButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_add_button))
        addButton.performClick()

        var title = composeTestRule.onNodeWithTag(getString(R.string.test_todo_title_box))
        var body = composeTestRule.onNodeWithTag(getString(R.string.test_todo_body_box))
        val saveButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_item_save_button))

        val titleText = "foo"
        val bodyText = "bar bar bar"
        title.performTextInput(titleText)
        body.performTextInput(bodyText)

        saveButton.performClick()

        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()

        val todo = composeTestRule.onAllNodesWithText(titleText).onFirst()
        todo.assertExists()
        todo.performClick()

        title = composeTestRule.onNodeWithTag(getString(R.string.test_todo_title_box))
        body = composeTestRule.onNodeWithTag(getString(R.string.test_todo_body_box))

        title.assertExists()
        body.assertExists()

        title.assertTextContains(titleText)
        body.assertTextContains(bodyText)

    }

    @Test
    fun navigateToTodoItemReminderScreen() {
        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()

        val addButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_add_button))
        addButton.performClick()

        var title = composeTestRule.onNodeWithTag(getString(R.string.test_todo_title_box))
        var body = composeTestRule.onNodeWithTag(getString(R.string.test_todo_body_box))
        val saveButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_item_save_button))

        val titleText = "foo"
        val bodyText = "bar bar bar"
        title.performTextInput(titleText)
        body.performTextInput(bodyText)

        saveButton.performClick()

        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()

        val todo = composeTestRule.onAllNodesWithText(titleText).onFirst()
        todo.assertExists()
        todo.performClick()

        val reminderButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_reminder_button))

        reminderButton.assertExists()
        reminderButton.performClick()

        composeTestRule
            .onNodeWithTag(getString(R.string.test_reminder_add_button))
            .assertExists()
        composeTestRule
            .onNodeWithTag(getString(R.string.test_reminder_back_button))
            .assertExists()

    }
}