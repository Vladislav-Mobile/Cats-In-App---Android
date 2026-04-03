package com.example.catsinapp.data

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import com.example.catsinapp.data.model.FeedingLog
import com.example.catsinapp.data.model.MealType
import org.json.JSONArray
import org.json.JSONObject

object FeedingLogRepository {

    private const val PREFS = "feeding_logs"
    private const val KEY   = "logs_json"

    // Map<"yyyy-MM-dd", List<FeedingLog>>
    val logs = mutableStateMapOf<String, List<FeedingLog>>()

    // Даты с записями — для точек в календаре
    val datesWithLogs = mutableStateMapOf<String, Boolean>()

    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        initialized = true
        loadFromPrefs(context)
    }

    fun observeDate(context: Context, dateKey: String) {
        // SharedPreferences — данные уже загружены в init()
    }

    fun addLog(context: Context, dateKey: String, log: FeedingLog) {
        val current = logs[dateKey] ?: emptyList()
        logs[dateKey] = current + log
        datesWithLogs[dateKey] = true
        saveToPrefs(context)
    }

    fun removeLog(context: Context, dateKey: String, logId: Int) {
        val current = logs[dateKey] ?: return
        val updated = current.filter { it.id != logId }
        if (updated.isEmpty()) {
            logs.remove(dateKey)
            datesWithLogs.remove(dateKey)
        } else {
            logs[dateKey] = updated
        }
        saveToPrefs(context)
    }

    // ── Сериализация ──────────────────────────────────────

    private fun saveToPrefs(context: Context) {
        val root = JSONObject()
        logs.forEach { (dateKey, list) ->
            val arr = JSONArray()
            list.forEach { log ->
                arr.put(JSONObject().apply {
                    put("id",     log.id)
                    put("type",   log.type.name)
                    put("food",   log.food)
                    put("amount", log.amount)
                    put("time",   log.time)
                })
            }
            root.put(dateKey, arr)
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY, root.toString()).apply()
    }

    private fun loadFromPrefs(context: Context) {
        val json = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, null) ?: return

        runCatching {
            val root = JSONObject(json)
            root.keys().forEach { dateKey ->
                val arr  = root.getJSONArray(dateKey)
                val list = mutableListOf<FeedingLog>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(FeedingLog(
                        id     = obj.getInt("id"),
                        type   = runCatching { MealType.valueOf(obj.getString("type")) }
                            .getOrDefault(MealType.SNACK),
                        food   = obj.getString("food"),
                        amount = obj.getInt("amount"),
                        time   = obj.getString("time")
                    ))
                }
                logs[dateKey]          = list
                datesWithLogs[dateKey] = true
            }
        }
    }
}