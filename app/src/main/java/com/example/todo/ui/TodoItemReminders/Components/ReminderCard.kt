package com.example.todo.ui.TodoItemReminders.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.Models.ReminderNotification
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun ReminderCard(
    reminderNotification: ReminderNotification,
    modifier: Modifier = Modifier
) {
    val date = ZonedDateTime.parse(reminderNotification.date)
    val contentPadding = Modifier.padding(4.dp)

    Row(
        Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date.format(DateTimeFormatter.ofPattern("HH:mm EEE")),
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            ),
            modifier = contentPadding
        )
        Text(
            text = date.format(DateTimeFormatter.ofPattern("dd/LLL")),
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            ),
            modifier = contentPadding
        )
    }
}

@Composable
@Preview
fun ReminderCardPreview() {
    val dummyReminder = ReminderNotification(
        "foo",
        "bar",
        UUID.randomUUID().toString(),
        todoId = 1,
        date = ZonedDateTime.now().toString()
    )

    ReminderCard(reminderNotification = dummyReminder)
}