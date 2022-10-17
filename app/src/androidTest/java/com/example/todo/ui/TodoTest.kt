package com.example.todo.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo.MainActivity
import com.example.todo.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TodoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)
    lateinit var getString: (Int) -> String

    @get:Rule var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        getString = composeTestRule.activity::getString
    }

    private fun createTodo(title: String, body: String) {
        composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_list_header))
            .assertIsDisplayed()
            .assertExists()

        val addButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_add_button))
        addButton.performClick()

        val titleField = composeTestRule.onNodeWithTag(getString(R.string.test_todo_title_box))
        val bodyField = composeTestRule.onNodeWithTag(getString(R.string.test_todo_body_box))
        val saveButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_item_save_button))

        titleField.performTextInput(title)
        bodyField.performTextInput(body)

        saveButton.performClick()

        composeTestRule.waitForIdle()
    }

    @Test
    fun createTodo() {
        composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_list_header))
            .assertIsDisplayed()
            .assertExists()

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

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(getString(R.string.test_todo_list_header)).assertExists()

        val todo = composeTestRule
            .onAllNodesWithTag("${getString(R.string.test_todo_list_item)}/${titleText}")
            .onFirst()
            .assertIsDisplayed()

        todo
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()

        title = composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_title_box), useUnmergedTree = true)
        body = composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_body_box), useUnmergedTree = true)


        title.assertIsDisplayed().assertExists()
        body.assertIsDisplayed().assertExists()

        title.assertTextContains(titleText)
        body.assertTextContains(bodyText)
    }

    @Test
    fun createReminder() {
        val titleText = "foo"
        val bodyText = "bar bar bar"
        createTodo(titleText, bodyText)

        val todo = composeTestRule
            .onAllNodesWithTag("${getString(R.string.test_todo_list_item)}/${titleText}")
            .onFirst()
            .assertIsDisplayed()

        todo
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()

        val reminderButton = composeTestRule.onNodeWithTag(getString(R.string.test_todo_reminder_button))
        reminderButton.assertExists()
        reminderButton.performClick()

        composeTestRule.waitForIdle()

        val addReminderButton = composeTestRule.onNodeWithTag(getString(R.string.test_reminder_add_button))
        addReminderButton.assertExists()
        addReminderButton.performClick()

        composeTestRule.waitForIdle()

        val dateSelectButton = composeTestRule.onNodeWithTag(getString(R.string.test_reminder_select_button))
        dateSelectButton.assertExists()
        dateSelectButton.performClick()

        composeTestRule.waitForIdle()

        val now = ZonedDateTime.now()
        val dateText = now.toLocalDate().format(DateTimeFormatter.ISO_DATE)

        composeTestRule.onNodeWithText(dateText, substring = true).assertExists()
    }

    @Test
    fun testTodoListIsCompletedListFilter() {
        val titleText = "foo"
        val bodyText = "bar bar bar"

        fun getTodoCheck() =
            composeTestRule
                .onNodeWithTag("${getString(R.string.test_todo_item_check_box)}/$titleText")



        createTodo(titleText, bodyText)

        val todo = composeTestRule
            .onAllNodesWithTag("${getString(R.string.test_todo_list_item)}/${titleText}")
            .onFirst()
            .assertIsDisplayed()


        val doneButton = composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_list_done_button))
        val notDoneButton = composeTestRule
            .onNodeWithTag(getString(R.string.test_todo_list_not_done_button))

        doneButton.assertIsDisplayed()
        notDoneButton.assertIsDisplayed()

        var todoCheck = getTodoCheck()

        todoCheck.performClick()

        todo.assertIsDisplayed()

        // not displayed when done
        notDoneButton.performClick()
        getTodoCheck().assertDoesNotExist()

        // displayed when done
        doneButton.performClick()
        getTodoCheck().assertIsDisplayed()

        // not displayed when not done
        getTodoCheck().performClick()
        composeTestRule.waitForIdle()
        getTodoCheck().assertDoesNotExist()

        // displayed when not done
        notDoneButton.performClick()
        getTodoCheck().assertIsDisplayed()

        // displayed when showing all todos
        getTodoCheck().performClick()
        notDoneButton.performClick()
        getTodoCheck().assertIsDisplayed()
    }

}