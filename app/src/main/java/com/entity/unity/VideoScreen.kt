package com.entity.unity

import android.Manifest
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.entity.unity.APP_ID
import com.entity.unity.MainActivity2
import com.entity.unity.VideoViewmodel
import com.example.entityyvc.ui.theme.AgoravcTheme
import io.agora.agorauikit_android.AgoraSettings

@ExperimentalUnsignedTypes
@Composable
fun VideoScreen(
    roomName:String,
    onNavigateUp:()-> Unit ={},
    viewmodel: VideoViewmodel = viewModel()

) {
    val context= LocalContext.current
    var agoraView: AgoraVideoViewer? = null
    val agoraSettings=AgoraSettings
    val permissionLauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(), onResult ={
        viewmodel.onPermissionsResult(
            acceptedAudioPermission = it[Manifest.permission.RECORD_AUDIO]==true,
            acceptedCameraPermission = it[Manifest.permission.CAMERA]==true


        )
    } )

    LaunchedEffect(key1 = true){
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        )
    }
    BackHandler {
        agoraView?.leaveChannel()

        context.startActivity(Intent(context,MainActivity2::class.java))
    }
    if(viewmodel._hasadPermi.value && viewmodel._hasCamPermi.value) {

        AndroidView(
            factory = {
                AgoraVideoViewer(
                    it, connectionData = AgoraConnectionData(
                        appId = APP_ID
                    )
                ).also {
                    it.join(roomName)
                    agoraView = it
                    viewmodel.alotcustom()
                }


            },
            modifier = Modifier.fillMaxSize(),


        )
    }




}