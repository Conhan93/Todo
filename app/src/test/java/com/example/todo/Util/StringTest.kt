package com.example.todo.Util

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*


class StringTest {

    @Test
    fun shouldReturnTrueOnUUID() {
        lateinit var uuid: String
        repeat(100) {
            uuid = UUID.randomUUID().toString()

            val result = uuid.isUUID()

            assertEquals(true, result)
        }
    }
}