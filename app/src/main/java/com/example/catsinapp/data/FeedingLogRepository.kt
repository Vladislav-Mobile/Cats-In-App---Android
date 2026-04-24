package com.example.catsinapp.data

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import com.example.catsinapp.data.db.AppDatabase
import com.example.catsinapp.data.db.FeedingLogDao
import com.example.catsinapp.data.db.FeedingLogEntity
import com.example.catsinapp.data.model.FeedingLog
import com.example.catsinapp.data.model.MealType
import com.example.catsinapp.debug.AppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

object FeedingLogRepository {

    // Старые ключи SharedPreferences — нужны только для однократной миграции
    private const val PREFS = "feeding_logs"
    private const val KEY   = "logs_json"

    // Compose-реактивные карты — UI подписывается на них напрямую
    val logs          = mutableStateMapOf<String, List<FeedingLog>>()
    val datesWithLogs = mutableStateMapOf<String, Boolean>()

    private var initialized = false
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // ── Инициализация ─────────────────────────────────────────────────────────

    fun init(context: Context) {
        if (initialized) return
        initialized = true

        scope.launch {
            val dao = AppDatabase.getInstance(context).feedingLogDao()

            // Если Room пустой — мигрируем данные из SharedPreferences (один раз)
            if (dao.totalCount() == 0) {
                migrateFromPrefs(context, dao)
            }

            // Загружаем всё из Room в StateMap (виден в Database Inspector)
            val all = dao.getAllLogs()
            withContext(Dispatchers.Main) {
                all.forEach { entity ->
                    val log = entity.toModel()
                    logs[entity.dateKey] = (logs[entity.dateKey] ?: emptyList()) + log
                    datesWithLogs[entity.dateKey] = true
                }
                val total = logs.values.sumOf { it.size }
                AppLogger.feedingLogsLoaded(totalDates = logs.size, totalLogs = total)
            }
        }
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    fun addLog(context: Context, dateKey: String, log: FeedingLog) {
        // Обновляем StateMap мгновенно — UI реагирует без задержки
        logs[dateKey] = (logs[dateKey] ?: emptyList()) + log
        datesWithLogs[dateKey] = true

        // Сохраняем в Room асинхронно — виден в Database Inspector
        scope.launch {
            AppDatabase.getInstance(context).feedingLogDao()
                .insertLog(log.toEntity(dateKey))
        }

        AppLogger.feedingLogAdded(
            type   = log.type.name,
            date   = dateKey,
            time   = log.time,
            food   = log.food,
            amount = log.amount
        )
    }

    fun removeLog(context: Context, dateKey: String, logId: Int) {
        val updated = (logs[dateKey] ?: return).filter { it.id != logId }
        if (updated.isEmpty()) {
            logs.remove(dateKey)
            datesWithLogs.remove(dateKey)
        } else {
            logs[dateKey] = updated
        }

        scope.launch {
            AppDatabase.getInstance(context).feedingLogDao().deleteLog(logId)
        }

        AppLogger.feedingLogDeleted(logId = logId, date = dateKey)
    }

    fun observeDate(context: Context, dateKey: String) { /* данные уже в StateMap */ }

    // ── Миграция SharedPreferences → Room (выполняется один раз) ─────────────

    private suspend fun migrateFromPrefs(context: Context, dao: FeedingLogDao) {
        val json = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, null) ?: return
        try {
            val root = JSONObject(json)
            root.keys().forEach { dateKey ->
                val arr = root.getJSONArray(dateKey)
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    dao.insertLog(FeedingLogEntity(
                        id      = obj.getInt("id"),
                        dateKey = dateKey,
                        type    = obj.getString("type"),
                        food    = obj.getString("food"),
                        amount  = obj.getInt("amount"),
                        time    = obj.getString("time")
                    ))
                }
            }
            // Очищаем SharedPreferences после успешной миграции
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().remove(KEY).apply()
        } catch (e: Exception) {
            AppLogger.jsonLoadError(prefs = PREFS, error = e)
        }
    }
}

// ── Конвертеры ────────────────────────────────────────────────────────────────

fun FeedingLog.toEntity(dateKey: String) = FeedingLogEntity(
    id      = id,
    dateKey = dateKey,
    type    = type.name,
    food    = food,
    amount  = amount,
    time    = time
)

fun FeedingLogEntity.toModel() = FeedingLog(
    id     = id,
    type   = runCatching { MealType.valueOf(type) }.getOrDefault(MealType.SNACK),
    food   = food,
    amount = amount,
    time   = time
)
