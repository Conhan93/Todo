package com.example.todo.Util

sealed class UIEvent {
    object popStackBack : UIEvent()
    data class navigate(val route : String) : UIEvent()
    data class ShowSnackBar(val message: String, val actionLabel: String? = null): UIEvent()
}
