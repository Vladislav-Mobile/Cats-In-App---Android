package com.example.catsinapp.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catsinapp.R
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.data.model.FeedingLog
import com.example.catsinapp.data.model.MealType
import com.example.catsinapp.ui.components.HomeHeader
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

// ─── Локальные цвета ──────────────────────────────────────
private val YellowCard  = Color(0xFFF5F7D0)
private val PeachCard   = Color(0xFFFDDCCC)
private val AmberLabel  = Color(0xFFB85C00)

// ─── Данные календаря (хардкод март 2026) ─────────────────
data class CalendarDay(
    val dayName: String,
    val dayNum: Int,
    val isSelected: Boolean,
    val hasDot: Boolean = false
)

private val calendarDays = listOf(
    CalendarDay("Mon", 23, false),
    CalendarDay("Tue", 24, false),
    CalendarDay("Wed", 25, true, hasDot = true),
    CalendarDay("Thu", 26, false),
    CalendarDay("Fri", 27, false)
)

// ─── Три состояния лога ───────────────────────────────────
private val state1 = listOf(
    FeedingLog("1", MealType.BREAKFAST,     "Dry Kibble",      "150ml",  "08:30 AM"),
    FeedingLog("2", MealType.MORNING_TREAT, "Dental Stick",    "1 unit", "11:15 AM"),
    FeedingLog("3", MealType.LUNCH,         "Wet Food (Beef)", "100g",   "01:45 PM")
)
private val state2 = listOf(
    FeedingLog("1", MealType.BREAKFAST,     "Dry Kibble",       "150ml",  "08:30 AM"),
    FeedingLog("2", MealType.MORNING_TREAT, "Wet Food (Beef)",  "100g",   "11:15 AM"),
    FeedingLog("3", MealType.LUNCH,         "Wet Food (Beef)",  "100g",   "01:45 PM"),
    FeedingLog("4", MealType.DINNER,        "Wet Food (Beef)",  "100g",   "01:45 PM"),
    FeedingLog("5", MealType.SNACK,         "Dental Stick",     "1 unit", "01:45 PM"),
    FeedingLog("6", MealType.SNACK,         "Dental Stick",     "1 unit", "01:45 PM")
)
private val state3 = emptyList<FeedingLog>()

@Composable
fun HistoryScreen() {
    // Переключаем состояния кнопкой View All (демо)
    var stateIndex by remember { mutableIntStateOf(0) }
    val logs = when (stateIndex) {
        0    -> state1
        1    -> state2
        else -> state3
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header с YouTube
        HomeHeader()

        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ── Feeding Schedule label ──────────────────────
            item {
                Text(
                    text          = "FEEDING SCHEDULE",
                    fontSize      = 11.sp,
                    color         = AmberLabel,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // ── Month + nav arrows ──────────────────────────
            item {
                Row(
                    modifier          = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Mart 2026",
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        modifier   = Modifier.weight(1f)
                    )
                    // Стрелки — UI only
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .border(1.dp, Color(0xFFD0E8B0), RoundedCornerShape(50.dp))
                            .clip(RoundedCornerShape(50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_left),
                            contentDescription = "Prev",
                            tint     = GreenDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .border(1.dp, Color(0xFFD0E8B0), RoundedCornerShape(50.dp))
                            .clip(RoundedCornerShape(50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_right),
                            contentDescription = "Next",
                            tint     = GreenDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // ── Calendar strip ──────────────────────────────
            item {
                LazyRow(
                    modifier              = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(calendarDays) { day ->
                        CalendarDayItem(day = day)
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // ── Summary cards ────────────────────────────────
            item {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total kcal
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(YellowCard, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter            = painterResource(R.drawable.ic_care_food),
                            contentDescription = null,
                            tint               = GreenDark,
                            modifier           = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Total Today", fontSize = 12.sp, color = TextSecondary)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text       = "450",
                                fontSize   = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary
                            )
                            Text(
                                text     = " kcal",
                                fontSize = 14.sp,
                                color    = TextSecondary,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }
                    // Water intake
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(PeachCard, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Text("💧", fontSize = 20.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("Water Intake", fontSize = 12.sp, color = TextSecondary)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text       = "1.2",
                                fontSize   = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary
                            )
                            Text(
                                text     = " liters",
                                fontSize = 14.sp,
                                color    = TextSecondary,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            // ── Today's Log header ───────────────────────────
            item {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Today's Log",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        modifier   = Modifier.weight(1f)
                    )
                    // View All переключает состояние (демо)
                    Text(
                        text     = "View All",
                        fontSize = 13.sp,
                        color    = GreenDark,
                        modifier = Modifier.clickable {
                            stateIndex = (stateIndex + 1) % 3
                        }
                    )
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // ── Лог кормлений ────────────────────────────────
            if (logs.isNotEmpty()) {
                items(logs, key = { it.id }) { log ->
                    FeedingLogItem(log = log)
                    HorizontalDivider(
                        color     = Color(0xFFF0F0F0),
                        thickness = 0.5.dp,
                        modifier  = Modifier.padding(start = 64.dp)
                    )
                }
            }

            // ── Log Next Meal button ─────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                LogNextMealButton()
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// CALENDAR DAY — один день в стрипе
// ─────────────────────────────────────────────────────────
@Composable
private fun CalendarDayItem(day: CalendarDay) {
    val bgColor   = if (day.isSelected) GreenDark else Color.Transparent
    val textColor = if (day.isSelected) Color.White else TextPrimary
    val nameColor = if (day.isSelected) Color.White else TextSecondary

    Column(
        modifier = Modifier
            .width(52.dp)
            .background(bgColor, RoundedCornerShape(26.dp))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(day.dayName, fontSize = 12.sp, color = nameColor)
        Text(
            text       = day.dayNum.toString(),
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = textColor
        )
        // Точка-индикатор
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(
                    if (day.hasDot) (if (day.isSelected) Color.White else GreenDark)
                    else Color.Transparent,
                    CircleShape
                )
        )
    }
}

// ─────────────────────────────────────────────────────────
// FEEDING LOG ITEM
// ─────────────────────────────────────────────────────────
@Composable
private fun FeedingLogItem(log: FeedingLog) {
    val (iconRes, iconBg) = mealIconData(log.type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка в цветном круге
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter            = painterResource(iconRes),
                contentDescription = log.type.label,
                tint               = Color(0xFF5A3A00),
                modifier           = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        // Название + описание
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = log.type.label,
                fontSize   = 15.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
            Row {
                Text(
                    text     = log.food,
                    fontSize = 12.sp,
                    color    = TextSecondary
                )
                Text(
                    text       = " • ${log.amount}",
                    fontSize   = 12.sp,
                    color      = GreenDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Время + стрелка
        Text(log.time, fontSize = 12.sp, color = TextSecondary)
        Spacer(Modifier.width(6.dp))
        Icon(
            painter            = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint               = Color(0xFFCCCCCC),
            modifier           = Modifier.size(16.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────
// LOG NEXT MEAL BUTTON
// ─────────────────────────────────────────────────────────
@Composable
private fun LogNextMealButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, Color(0xFFDDDDDD), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* UI only */ }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(GreenDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter            = painterResource(R.drawable.ic_add_circle),
                    contentDescription = "Add",
                    tint               = Color.White,
                    modifier           = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text       = "Log Next Meal",
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = GreenDark
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// Иконка + цвет фона для каждого типа кормления
// ─────────────────────────────────────────────────────────
private fun mealIconData(type: MealType): Pair<Int, Color> = when (type) {
    MealType.BREAKFAST     -> R.drawable.ic_care_food    to Color(0xFFDFF5E0)
    MealType.MORNING_TREAT -> R.drawable.ic_care_food    to Color(0xFFFFF0DC)
    MealType.LUNCH         -> R.drawable.ic_care_food    to Color(0xFFFDE8E8)
    MealType.DINNER        -> R.drawable.ic_care_food    to Color(0xFFDDEEFF)
    MealType.SNACK         -> R.drawable.ic_care_food    to Color(0xFFF3E0FF)
}