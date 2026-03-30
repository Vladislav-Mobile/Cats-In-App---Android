package com.example.catsinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.example.catsinapp.debug.DevMenuDialog
import com.example.catsinapp.debug.ShakeDetector
import com.example.catsinapp.ui.navigation.AppNavigation
import com.example.catsinapp.ui.theme.CatsInAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CatsInAppTheme {
                var devMenuVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    shakeDetector = ShakeDetector {
                        devMenuVisible = !devMenuVisible
                    }
                    ShakeDetector.register(this@MainActivity, shakeDetector)
                }

                // Основная навигация
                AppNavigation()

                // Dev panel — поверх всего
                DevMenuDialog(
                    visible   = devMenuVisible,
                    onDismiss = { devMenuVisible = false }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::shakeDetector.isInitialized) {
            ShakeDetector.unregister(this, shakeDetector)
        }
    }
}