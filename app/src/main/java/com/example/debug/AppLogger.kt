package com.example.catsinapp.debug

import android.app.ActivityManager
import android.content.Context
import android.os.Trace
import android.util.Log

object AppLogger {

    private const val TAG = "CatsApp"

    // ─── Навигация ─────────────────────────────────────────

    fun screenOpened(screenName: String, args: String? = null) {
        val msg = if (args != null) "→ $screenName | args=$args" else "→ $screenName"
        Log.i("$TAG/NAV", msg)
    }

    fun screenLoadTime(screenName: String, ms: Long) {
        Log.d("$TAG/PERF", "⏱ $screenName loaded in ${ms}ms")
    }

    // ─── Жизненный цикл Activity ───────────────────────────

    fun activityCreated(activityName: String) {
        Log.d("$TAG/LIFECYCLE", "▶ onCreate: $activityName")
        Trace.beginSection("${activityName}_onCreate")
        Trace.endSection()
    }

    fun activityStarted(activityName: String) {
        Log.d("$TAG/LIFECYCLE", "▶ onStart: $activityName")
    }

    fun activityResumed(activityName: String) {
        Log.d("$TAG/LIFECYCLE", "▶ onResume: $activityName")
        Trace.beginSection("${activityName}_onResume")
        Trace.endSection()
    }

    fun activityPaused(activityName: String) {
        Log.d("$TAG/LIFECYCLE", "⏸ onPause: $activityName")
    }

    fun activityStopped(activityName: String) {
        Log.d("$TAG/LIFECYCLE", "⏹ onStop: $activityName")
    }

    fun activityDestroyed(activityName: String) {
        Log.d("$TAG/LIFECYCLE", "✖ onDestroy: $activityName")
    }

    fun activitySaveState(activityName: String) {
        Log.d("$TAG/LIFECYCLE", "💾 onSaveInstanceState: $activityName")
    }

    fun lowMemoryWarning() {
        Log.w("$TAG/LIFECYCLE", "⚠️ onLowMemory — система запрашивает освобождение памяти")
    }

    fun trimMemory(level: Int) {
        val levelName = when (level) {
            80   -> "COMPLETE (80) — фон, освободи всё"
            60   -> "MODERATE (60) — фон, умеренно"
            40   -> "BACKGROUND (40) — фон"
            20   -> "UI_HIDDEN (20) — UI скрыт"
            15   -> "RUNNING_CRITICAL (15) — критично"
            10   -> "RUNNING_LOW (10) — мало памяти"
            5    -> "RUNNING_MODERATE (5) — умеренно"
            else -> "UNKNOWN ($level)"
        }
        Log.w("$TAG/LIFECYCLE", "🔻 onTrimMemory: $levelName")
    }

    // ─── Память ────────────────────────────────────────────

    fun logMemoryStats(context: Context) {
        val runtime = Runtime.getRuntime()
        val usedMb  = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L
        val maxMb   = runtime.maxMemory() / 1048576L
        val freeMb  = runtime.freeMemory() / 1048576L

        Log.i("$TAG/MEMORY", "📊 Heap: used=${usedMb}MB | free=${freeMb}MB | max=${maxMb}MB")

        val am   = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        val availMb = info.availMem / 1048576L
        val totalMb = info.totalMem / 1048576L

        Log.i("$TAG/MEMORY", "📊 Device: avail=${availMb}MB | total=${totalMb}MB | lowMemory=${info.lowMemory}")

        if (maxMb > 0 && usedMb.toFloat() / maxMb.toFloat() > 0.8f) {
            Log.w("$TAG/MEMORY", "⚠️ High memory: ${usedMb}MB / ${maxMb}MB (${usedMb * 100 / maxMb}%)")
        }
    }

    // ─── Действия пользователя ─────────────────────────────

    fun petTapped(petId: String, petName: String) {
        Log.i("$TAG/ACTION", "🐱 petTapped | id=$petId | name=$petName")
    }

    fun feedingLogAdded(type: String, date: String, time: String, food: String, amount: Int) {
        Log.i("$TAG/ACTION", "🍽 feedingLogAdded | type=$type | date=$date | time=$time | food=$food | amount=${amount}g")
    }

    fun feedingLogDeleted(logId: Int, date: String) {
        Log.i("$TAG/ACTION", "🗑 feedingLogDeleted | id=$logId | date=$date")
    }

    fun profileSaved(name: String, dob: String, weightKg: String, weightG: String, hasPhoto: Boolean) {
        Log.i("$TAG/ACTION", "👤 profileSaved | name=$name | dob=$dob | weight=${weightKg}kg ${weightG}g | photo=$hasPhoto")
    }

    fun careBottomSheetOpened(categoryId: String, categoryTitle: String) {
        Log.i("$TAG/ACTION", "💡 careBottomSheet | id=$categoryId | title=$categoryTitle")
    }

    // ─── Данные ────────────────────────────────────────────

    fun feedingLogsLoaded(totalDates: Int, totalLogs: Int) {
        Log.i("$TAG/DATA", "📦 feedingLogsLoaded | dates=$totalDates | totalLogs=$totalLogs")
    }

    fun photoLoaded(success: Boolean, uri: String) {
        if (success) Log.i("$TAG/DATA", "🖼 photoLoaded OK | uri=$uri")
        else         Log.w("$TAG/DATA", "🖼 photoLoaded FAILED | uri=$uri")
    }

    fun internetCheck(connected: Boolean, screen: String) {
        if (connected) Log.i("$TAG/DATA", "🌐 internet=CONNECTED | screen=$screen")
        else           Log.w("$TAG/DATA", "🌐 internet=DISCONNECTED | screen=$screen")
    }

    // ─── Ошибки ────────────────────────────────────────────

    fun jsonLoadError(prefs: String, error: Throwable) {
        Log.e("$TAG/ERROR", "❌ jsonLoadError | prefs=$prefs | ${error.message}", error)
    }

    fun webViewError(url: String, errorCode: Int, description: String) {
        Log.e("$TAG/ERROR", "❌ webViewError | url=$url | code=$errorCode | desc=$description")
    }

    // ─── Android Profiler ──────────────────────────────────
    // Убран inline — используем обычную функцию высшего порядка.
    // Для Profiler секции используйте beginTrace/endTrace напрямую.

    fun <T> trace(sectionName: String, block: () -> T): T {
        Trace.beginSection(sectionName)
        val start = System.currentTimeMillis()
        return try {
            block()
        } finally {
            val ms = System.currentTimeMillis() - start
            Trace.endSection()
            Log.v("$TAG/PERF", "◼ trace[$sectionName] ${ms}ms")
        }
    }

    fun beginTrace(sectionName: String) {
        Trace.beginSection(sectionName)
        Log.v("$TAG/PERF", "▶ trace begin: $sectionName")
    }

    fun endTrace(sectionName: String) {
        Trace.endSection()
        Log.v("$TAG/PERF", "■ trace end: $sectionName")
    }
}