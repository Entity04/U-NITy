package com.entity.unity

import android.Manifest
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.entity.unity.constants.Constants
import com.entity.unity.counsellorChat.CounsellorHome
import com.entity.unity.videocall.APP_ID
import com.google.firebase.auth.FirebaseAuth
import io.agora.agorauikit_android.AgoraSettings
import com.entity.unity.videocall.VideoActivity
import com.entity.unity.videocall.VideoViewmodel


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
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser!!.uid
        }
        if(Constants.hashmap[currentUserId]==true)
            context.startActivity(Intent(context, CounsellorHome::class.java))
        else {
            context.startActivity(Intent(context, MainActivity2::class.java))
        }
    }
    if(viewmodel._hasadPermi.value && viewmodel._hasCamPermi.value) {

        AndroidView(
            factory = {
                AgoraVideoViewer(
                    it, connectionData = AgoraConnectionData(
                        appId = APP_ID,
                    ),agoraSettings= VideoActivity().uisettings()
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