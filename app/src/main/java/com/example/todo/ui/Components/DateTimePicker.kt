package com.example.todo.ui.Components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.R
import java.time.ZonedDateTime


@Composable
fun DateTimePicker(
    modifier: Modifier = Modifier,
    onCancel: (() -> Unit)? = null,
    onSelect: (ZonedDateTime) -> Unit
) {
    val time = remember { mutableStateOf(ZonedDateTime.now()) }
    Column(modifier = modifier) {
        Row() {
            // Time Spinner
            SpinnerCell(
                value = time.value.hour.toString(),
                onClickUp = { time.value = time.value.plusHours(1) },
                onClickDown = { time.value = time.value.minusHours(1) }
            )
            SpinnerCell(
                value = time.value.minute.toString(),
                onClickUp = { time.value = time.value.plusMinutes(1) },
                onClickDown = { time.value = time.value.minusMinutes(1) }
            )
            SpinnerCell(
                value = time.value.second.toString(),
                onClickUp = { time.value = time.value.plusSeconds(1) },
                onClickDown = { time.value = time.value.minusSeconds(1) }
            )

            // Date Spinner
            SpinnerCell(
                value = time.value.dayOfMonth.toString(),
                onClickUp = { time.value = time.value.plusDays(1) },
                onClickDown = { time.value = time.value.minusDays(1) }
            )
            SpinnerCell(
                value = time.value.month.toString(),
                onClickUp = { time.value = time.value.plusMonths(1) },
                onClickDown = { time.value = time.value.minusMonths(1) }
            )
            SpinnerCell(
                value = time.value.year.toString(),
                onClickUp = { time.value = time.value.plusYears(1) },
                onClickDown = { time.value = time.value.minusYears(1) }
            )
        }
        Row() {
            onCancel?.let {
                TextButton(onClick = { it() }) {
                    Text("Cancel")
                }
            }
            TextButton(
                onClick = { onSelect(time.value) },
                modifier = Modifier.testTag(stringResource(R.string.test_reminder_select_button))
            ) {
                Text("Select")
            }
        }
    }
}


@Composable
private fun SpinnerCell(
    value: String,
    onClickUp: () -> Unit,
    onClickDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        IconButton(onClick = { onClickUp() }) {
            Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = null)
        }
        Text(value)
        IconButton(onClick = {onClickDown()}) {
            Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null)
        }
    }
}




@Preview
@Composable
private fun DateTimePickerPreview() {
    DateTimePicker(onCancel = { Log.d("DatetimepickerPreview", "DateTimePickerPreview: canceled")}) {
        Log.d("DatetimepickerPreview", "DateTimePickerPreview: selected ${it}")
    }
}