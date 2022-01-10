package com.entity.unity

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

import androidx.compose.ui.unit.dp

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


import com.example.entityyvc.ui.theme.AgoravcTheme
import com.example.entityyvc.ui.theme.RoomScreen
import java.lang.reflect.Modifier

const val APP_ID = "dc9ae93855584b78a0788d17227ee785"

@ExperimentalUnsignedTypes
class VideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgoravcTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = "room_screen"
                    ) {
                        composable(route = "room_screen") {
                            RoomScreen(onNavigate = navController::navigate)
                        }
                        composable(
                            route = "video_screen/{roomName}",
                            arguments = listOf(
                                navArgument(name = "roomName") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val roomName = it.arguments?.getString("roomName") ?: return@composable
                            VideoScreen(
                                roomName = roomName,
                                onNavigateUp = navController::navigateUp
                            )
                        }
                    }
                }
            }
        }
    }
}