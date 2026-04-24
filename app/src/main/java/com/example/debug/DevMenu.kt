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
import com.example.catsinapp.data.network.CatFactsRepository
import com.example.catsinapp.data.db.AppDatabase
import com.example.catsinapp.data.db.FeedingLogEntity
import com.example.catsinapp.data.db.PetProfileEntity
import kotlinx.coroutines.launch
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
            // Кнопка отправляет OkHttp-запрос прямо сейчас —
            // открой Network Inspector и нажми кнопку, чтобы увидеть трафик
            DevMenuButton(label = "🌐 Ping Network (для Network Inspector)") {
                scope.launch { CatFactsRepository.fetchFact() }
                onDismiss()
            }

            // ── Database Inspector ─────────────────────────────────────────────
            // Заполняет обе таблицы тестовыми данными —
            // после нажатия открой Database Inspector, кликни на таблицу
            // и увидишь строки (двойной клик по ячейке = редактирование)
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