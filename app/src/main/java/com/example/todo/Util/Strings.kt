package com.example.todo.Util


fun String.isUUID(): Boolean {
    val pattern = """^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}${'$'}""".toRegex()

    return pattern.matches(this)
}