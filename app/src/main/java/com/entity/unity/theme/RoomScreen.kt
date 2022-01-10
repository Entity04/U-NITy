package com.example.entityyvc.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.entity.unity.RoomViewModel

import kotlinx.coroutines.flow.collectLatest

@Composable
fun RoomScreen(
onNavigate:(String)->Unit,
viewModel: RoomViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
LaunchedEffect(key1 = true){
    viewModel.onJoinEvent.collectLatest {
        onNavigate("video_screen/$it")
    }
}
    Column(
        modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.End
    ) {
TextField(value = viewModel.roomname.value.text, onValueChange =viewModel::onroomEnter
, modifier = Modifier.fillMaxWidth(),
    isError = viewModel.roomname.value.error!=null,
    placeholder = {
        Text(text = "PLease Enter RoomName")
    }

)
        viewModel.roomname.value.error?.let {
            Text(text = it, color = MaterialTheme.colors.error)
        }


Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = viewModel::onJoinRoom) {
            Text(text = "Join")
        }
    }
}
