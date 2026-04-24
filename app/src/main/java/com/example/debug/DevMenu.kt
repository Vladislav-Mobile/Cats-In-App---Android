package com.example.catsinapp.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.catsinapp.data.network.CatFactsRepository
import com.example.catsinapp.data.network.GitHubRepository
import com.example.catsinapp.data.db.AppDatabase
import com.example.catsinapp.data.db.FeedingLogEntity
import com.example.catsinapp.data.db.PetProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate

private val GreenDark = Color(0xFF2D5016)

@Composable
fun DevMenuDialog(visible: Boolean, onDismiss: () -> Unit) {
    if (!visible) return
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text       = "🛠 Dev Menu",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = GreenDark
            )

            HorizontalDivider(color = Color(0xFFEEEEEE))

            // ── Network Inspector ──────────────────────────────────────────────
            // Отправляет 3 OkHttp-запроса сразу — catfact.ninja + GitHub API x2.
            // ⚠️ WebView-трафик (YouTube, Wikipedia) в Network Inspector НЕ виден:
            // WebView использует Chromium, а не OkHttp — это ограничение Android Studio.
            DevMenuButton(label = "🌐 Ping Network × 3 (для Network Inspector)") {
                scope.launch {
                    CatFactsRepository.fetchFact()        // GET catfact.ninja/fact
                    GitHubRepository.fetchRepoStats()     // GET api.github.com/repos/...
                    GitHubRepository.fetchBranches()      // GET api.github.com/repos/.../branches
                }
                onDismiss()
            }

            // ── Export DB ─────────────────────────────────────────────────────
            // Экспортирует файл .db через стандартный Android share-диалог.
            // Можно сохранить на ПК, в Google Drive, Telegram и т.д.
            // Для импорта: замени файл вручную через Device Explorer в Android Studio.
            DevMenuButton(label = "📤 Экспорт БД (поделиться .db файлом)") {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val db = AppDatabase.getInstance(context)
                            // Checkpoint WAL перед копированием
                            db.openHelper.writableDatabase
                                .execSQL("PRAGMA wal_checkpoint(FULL)")
                            val src  = context.getDatabasePath("cats_app_db")
                            val dest = File(context.cacheDir, "cats_app_db_export.db")
                            src.copyTo(dest, overwrite = true)
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                dest
                            )
                            withContext(Dispatchers.Main) {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/octet-stream"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    putExtra(Intent.EXTRA_SUBJECT, "cats_app_db.db")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(
                                    Intent.createChooser(intent, "Экспорт базы данных")
                                )
                            }
                        } catch (e: Exception) {
                            AppLogger.jsonLoadError(prefs = "db_export", error = e)
                        }
                    }
                }
                onDismiss()
            }

            // ── 50 записей кормлений (24–28 апреля 2026) ──────────────────────
            DevMenuButton(label = "📅 Загрузить 50 записей (24–28 апр 2026)") {
                scope.launch {
                    val dao = AppDatabase.getInstance(context).feedingLogDao()
                    listOf(
                        // 24 апреля
                        FeedingLogEntity(3001,"2026-04-24","BREAKFAST","Whiskas курица",80,"07:30"),
                        FeedingLogEntity(3002,"2026-04-24","SNACK","Dreamies лакомство",15,"10:00"),
                        FeedingLogEntity(3003,"2026-04-24","LUNCH","Felix говядина",70,"12:30"),
                        FeedingLogEntity(3004,"2026-04-24","SNACK","Brit Premium паштет",30,"14:00"),
                        FeedingLogEntity(3005,"2026-04-24","DINNER","Sheba тунец",85,"17:00"),
                        FeedingLogEntity(3006,"2026-04-24","SNACK","Purina One сухой",20,"19:00"),
                        FeedingLogEntity(3007,"2026-04-24","DINNER","RC Adult сухой",90,"20:00"),
                        FeedingLogEntity(3008,"2026-04-24","SNACK","Animonda Carny",25,"21:30"),
                        FeedingLogEntity(3009,"2026-04-24","BREAKFAST","Leonardo лосось",75,"08:00"),
                        FeedingLogEntity(3010,"2026-04-24","LUNCH","Hills Science Diet",65,"13:00"),
                        // 25 апреля
                        FeedingLogEntity(3011,"2026-04-25","BREAKFAST","Whiskas рыба",80,"07:45"),
                        FeedingLogEntity(3012,"2026-04-25","SNACK","Dreamies курица",15,"09:30"),
                        FeedingLogEntity(3013,"2026-04-25","LUNCH","Felix суп тунец",50,"12:00"),
                        FeedingLogEntity(3014,"2026-04-25","SNACK","Brit Care мусс",30,"14:30"),
                        FeedingLogEntity(3015,"2026-04-25","DINNER","Sheba паштет",90,"17:30"),
                        FeedingLogEntity(3016,"2026-04-25","SNACK","Purina Felix",20,"19:30"),
                        FeedingLogEntity(3017,"2026-04-25","DINNER","RC Kitten сухой",85,"20:30"),
                        FeedingLogEntity(3018,"2026-04-25","SNACK","Animonda тунец",25,"22:00"),
                        FeedingLogEntity(3019,"2026-04-25","BREAKFAST","Leonardo говядина",70,"08:15"),
                        FeedingLogEntity(3020,"2026-04-25","LUNCH","Hills Urinary Care",60,"13:30"),
                        // 26 апреля
                        FeedingLogEntity(3021,"2026-04-26","BREAKFAST","Whiskas ягнёнок",80,"07:30"),
                        FeedingLogEntity(3022,"2026-04-26","SNACK","Dreamies сыр",15,"10:15"),
                        FeedingLogEntity(3023,"2026-04-26","LUNCH","Felix кролик",70,"12:15"),
                        FeedingLogEntity(3024,"2026-04-26","SNACK","Brit Premium курица",35,"14:00"),
                        FeedingLogEntity(3025,"2026-04-26","DINNER","Sheba говядина",85,"17:00"),
                        FeedingLogEntity(3026,"2026-04-26","SNACK","Purina One Indoor",20,"19:15"),
                        FeedingLogEntity(3027,"2026-04-26","DINNER","RC Sterilised",90,"20:15"),
                        FeedingLogEntity(3028,"2026-04-26","SNACK","Animonda лосось",20,"21:45"),
                        FeedingLogEntity(3029,"2026-04-26","BREAKFAST","Leonardo тунец",75,"08:30"),
                        FeedingLogEntity(3030,"2026-04-26","LUNCH","Hills Sensitive",65,"13:15"),
                        // 27 апреля
                        FeedingLogEntity(3031,"2026-04-27","BREAKFAST","Whiskas курица",80,"07:00"),
                        FeedingLogEntity(3032,"2026-04-27","SNACK","Dreamies лосось",15,"09:45"),
                        FeedingLogEntity(3033,"2026-04-27","LUNCH","Felix говядина суп",55,"12:30"),
                        FeedingLogEntity(3034,"2026-04-27","SNACK","Brit Care паштет",30,"14:15"),
                        FeedingLogEntity(3035,"2026-04-27","DINNER","Sheba лосось",90,"17:15"),
                        FeedingLogEntity(3036,"2026-04-27","SNACK","Purina Gourmet",25,"19:00"),
                        FeedingLogEntity(3037,"2026-04-27","DINNER","RC Light сухой",85,"20:00"),
                        FeedingLogEntity(3038,"2026-04-27","SNACK","Animonda говядина",20,"21:30"),
                        FeedingLogEntity(3039,"2026-04-27","BREAKFAST","Leonardo курица",75,"08:00"),
                        FeedingLogEntity(3040,"2026-04-27","LUNCH","Hills Indoor",60,"13:00"),
                        // 28 апреля
                        FeedingLogEntity(3041,"2026-04-28","BREAKFAST","Whiskas тунец",80,"07:15"),
                        FeedingLogEntity(3042,"2026-04-28","SNACK","Dreamies говядина",15,"10:00"),
                        FeedingLogEntity(3043,"2026-04-28","LUNCH","Felix паштет",70,"12:45"),
                        FeedingLogEntity(3044,"2026-04-28","SNACK","Brit Premium рыба",30,"14:30"),
                        FeedingLogEntity(3045,"2026-04-28","DINNER","Sheba курица",85,"17:30"),
                        FeedingLogEntity(3046,"2026-04-28","SNACK","Purina Felix сухой",20,"19:45"),
                        FeedingLogEntity(3047,"2026-04-28","DINNER","RC Hairball сухой",90,"20:45"),
                        FeedingLogEntity(3048,"2026-04-28","SNACK","Animonda кролик",25,"22:00"),
                        FeedingLogEntity(3049,"2026-04-28","BREAKFAST","Leonardo говядина",75,"08:45"),
                        FeedingLogEntity(3050,"2026-04-28","LUNCH","Hills Kitten",65,"13:45")
                    ).forEach { dao.insertLog(it) }
                }
                onDismiss()
            }

            // ── Database Inspector ─────────────────────────────────────────────
            DevMenuButton(label = "🗄️ Fill Test Data (для DB Inspector)") {
                scope.launch {
                    val db  = AppDatabase.getInstance(context)
                    val today     = LocalDate.now().toString()
                    val yesterday = LocalDate.now().minusDays(1).toString()

                    // pet_profile
                    db.petProfileDao().saveProfile(
                        PetProfileEntity(
                            id        = 1,
                            name      = "Мурзик",
                            birthDate = "15/03/2022",
                            weightKg  = "4",
                            weightG   = "300",
                            photoUri  = ""
                        )
                    )

                    // feeding_logs — несколько записей для двух дней
                    listOf(
                        FeedingLogEntity(id = 2001, dateKey = today,     type = "BREAKFAST", food = "Whiskas курица",     amount = 80, time = "08:00"),
                        FeedingLogEntity(id = 2002, dateKey = today,     type = "LUNCH",     food = "Felix суп",          amount = 40, time = "13:00"),
                        FeedingLogEntity(id = 2003, dateKey = today,     type = "DINNER",    food = "Sheba паштет",       amount = 85, time = "19:30"),
                        FeedingLogEntity(id = 2004, dateKey = yesterday, type = "BREAKFAST", food = "Whiskas рыба",       amount = 75, time = "08:30"),
                        FeedingLogEntity(id = 2005, dateKey = yesterday, type = "DINNER",    food = "Felix говядина",     amount = 80, time = "19:00")
                    ).forEach { db.feedingLogDao().insertLog(it) }
                }
                onDismiss()
            }

            HorizontalDivider(color = Color(0xFFEEEEEE))

            // Collect Logs
            DevMenuButton(label = "📋 Collect Logs") {
                LogCollector.collectAndShare(context)
                onDismiss()
            }

            // Memory Stats
            DevMenuButton(label = "📊 Log Memory Stats") {
                AppLogger.logMemoryStats(context)
                onDismiss()
            }

            // Crash App
            DevMenuButton(
                label = "💥 Crash App",
                color = Color(0xFFFF4444)
            ) {
                throw RuntimeException("[DevMenu] Manual crash triggered for testing")
            }

            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.Gray)
            }
        }
    }
}

@Composable
private fun DevMenuButton(
    label: String,
    color: Color = GreenDark,
    onClick: () -> Unit
) {
    Button(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}