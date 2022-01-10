package com.entity.unity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.agora.agorauikit_android.AgoraButton
import io.agora.agorauikit_android.AgoraSettings

class VideoViewmodel:ViewModel() {
    private val _hasadPerm= mutableStateOf(false)
    val _hasadPermi:State<Boolean> =_hasadPerm
    private val _hasCamPerm= mutableStateOf(false)
    val _hasCamPermi:State<Boolean> =_hasadPerm
    fun onPermissionsResult(
        acceptedAudioPermission: Boolean,
        acceptedCameraPermission: Boolean
    ) {
        _hasadPerm.value = acceptedAudioPermission
        _hasCamPerm.value = acceptedCameraPermission
    }
    fun alotcustom(): AgoraSettings {
        val agoras= AgoraSettings()
        agoras.enabledButtons= mutableSetOf(
            AgoraSettings.BuiltinButton.CAMERA,
            AgoraSettings.BuiltinButton.MIC,
            AgoraSettings.BuiltinButton.END
        )
        return agoras

    }
}