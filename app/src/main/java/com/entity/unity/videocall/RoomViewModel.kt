package com.entity.unity.videocall

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RoomViewModel:ViewModel() {
    private val _roomname= mutableStateOf(Textfield())
    val roomname:State<Textfield> = _roomname


    private val _onJoinEvent = MutableSharedFlow<String>()
    val onJoinEvent = _onJoinEvent.asSharedFlow()
fun onroomEnter(name:String){
    _roomname.value=_roomname.value.copy(
        text = name
    )
}
    fun onJoinRoom() {
        if(roomname.value.text.isBlank()) {
            _roomname.value = roomname.value.copy(
                error = "The room can't be empty"
            )
            return
        }
        viewModelScope.launch {
            _onJoinEvent.emit(roomname.value.text)
        }
    }
}