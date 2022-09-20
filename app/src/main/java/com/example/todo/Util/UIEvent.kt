package com.example.todo.Util

sealed class UIEvent {
    object popStackBack : UIEvent()
    data class navigate(val route : String) : UIEvent()
}
