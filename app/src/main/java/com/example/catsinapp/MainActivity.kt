package com.example.catsinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.catsinapp.data.FeedingLogRepository
import com.example.catsinapp.data.network.CatFactsRepository
import com.example.catsinapp.debug.AppLogger
import com.example.catsinapp.debug.DevMenuDialog
import com.example.catsinapp.debug.ShakeDetector
import com.example.catsinapp.ui.navigation.AppNavigation
import com.example.catsinapp.ui.theme.CatsInAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var shakeDetector: ShakeDetector

    // ─── onCreate ─────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        AppLogger.activityCreated("MainActivity")

        // Загружаем данные кормления из Room
        FeedingLogRepository.init(this)

        // Сетевой запрос при старте — виден в Network Inspector
        // (не ждём результата, просто пинг для инспектора)
        lifecycleScope.launch {
            CatFactsRepository.fetchFact()
        }

        setContent {
            CatsInAppTheme {
                var devMenuVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    shakeDetector = ShakeDetector { devMenuVisible = !devMenuVisible }
                    ShakeDetector.register(this@MainActivity, shakeDetector)
                }

                AppNavigation()

                DevMenuDialog(
                    visible   = devMenuVisible,
                    onDismiss = { devMenuVisible = false }
                )
            }
        }
    }

    // ─── onStart ──────────────────────────────────────────
    override fun onStart() {
        super.onStart()
        AppLogger.activityStarted("MainActivity")
    }

    // ─── onResume ─────────────────────────────────────────
    override fun onResume() {
        super.onResume()
        AppLogger.activityResumed("MainActivity")
        // Логируем состояние памяти при каждом возврате в приложение
        AppLogger.logMemoryStats(this)
    }

    // ─── onPause ──────────────────────────────────────────
    override fun onPause() {
        super.onPause()
        AppLogger.activityPaused("MainActivity")
    }

    // ─── onStop ───────────────────────────────────────────
    override fun onStop() {
        super.onStop()
        AppLogger.activityStopped("MainActivity")
    }

    // ─── onSaveInstanceState ──────────────────────────────
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        AppLogger.activitySaveState("MainActivity")
    }

    // ─── onDestroy ────────────────────────────────────────
    override fun onDestroy() {
        super.onDestroy()
        AppLogger.activityDestroyed("MainActivity")
        if (::shakeDetector.isInitialized) {
            ShakeDetector.unregister(this, shakeDetector)
        }
    }

    // ─── onLowMemory ──────────────────────────────────────
    // Вызывается системой когда устройству критически не хватает памяти
    override fun onLowMemory() {
        super.onLowMemory()
        AppLogger.lowMemoryWarning()
        AppLogger.logMemoryStats(this)
    }

    // ─── onTrimMemory ─────────────────────────────────────
    // Вызывается с разными уровнями — от "умеренно" до "критично"
    // Видно в App Inspection → Memory Profiler как GC events
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        AppLogger.trimMemory(level)
        AppLogger.logMemoryStats(this)
    }
}