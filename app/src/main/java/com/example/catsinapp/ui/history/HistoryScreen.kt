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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catsinapp.R
import com.example.catsinapp.data.FeedingLogRepository
import com.example.catsinapp.data.model.FeedingLog
import com.example.catsinapp.data.model.MealType
import com.example.catsinapp.ui.components.HomeHeader
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val YellowCard = Color(0xFFF5F7D0)
private val PeachCard  = Color(0xFFFDDCCC)

private fun mealIconRes(type: MealType): Int = when (type) {
    MealType.BREAKFAST     -> R.drawable.ic_meal_breakfast
    MealType.MORNING_TREAT -> R.drawable.ic_meal_treat
    MealType.LUNCH         -> R.drawable.ic_meal_lunch
    MealType.DINNER        -> R.drawable.ic_meal_dinner
    MealType.SNACK         -> R.drawable.ic_meal_snack
}

private fun mealIconBg(type: MealType): Color = when (type) {
    MealType.BREAKFAST     -> Color(0xFFDFF5E0)
    MealType.MORNING_TREAT -> Color(0xFFFFF0DC)
    MealType.LUNCH         -> Color(0xFFFDE8E8)
    MealType.DINNER        -> Color(0xFFDDEEFF)
    MealType.SNACK         -> Color(0xFFF3E0FF)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    val context       = LocalContext.current
    val today         = remember { LocalDate.now() }
    var currentMonth  by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate  by remember { mutableStateOf(today) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { FeedingLogRepository.init(context) }

    val dateKey                      = selectedDate.toString()
    val selectedLogs: List<FeedingLog> = FeedingLogRepository.logs[dateKey] ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        HomeHeader()

        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Text("FEEDING SCHEDULE", fontSize = 11.sp, color = Color(0xFFB85C00),
                    fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }

            // Месяц + Today + стрелки
            item {
                Row(
                    modifier          = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)),
                        fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = TextPrimary, modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp)).background(YellowCard)
                            .clickable { selectedDate = today; currentMonth = YearMonth.now() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Today", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                    }
                    Spacer(Modifier.width(8.dp))
                    MonthNavBtn(R.drawable.ic_chevron_left)  { currentMonth = currentMonth.minusMonths(1) }
                    Spacer(Modifier.width(6.dp))
                    MonthNavBtn(R.drawable.ic_chevron_right) { currentMonth = currentMonth.plusMonths(1) }
                }
            }

            // Calendar strip
            item {
                val days = (-3..3).map { selectedDate.plusDays(it.toLong()) }
                LazyRow(
                    modifier              = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(days) { date ->
                        val isSelected = date == selectedDate
                        val hasDot     = FeedingLogRepository.datesWithLogs[date.toString()] == true
                        Column(
                            modifier = Modifier
                                .width(52.dp)
                                .background(
                                    if (isSelected) GreenDark else Color.Transparent,
                                    RoundedCornerShape(26.dp)
                                )
                                .clickable {
                                    selectedDate = date
                                    currentMonth = YearMonth.of(date.year, date.month)
                                }
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                                fontSize = 12.sp,
                                color    = if (isSelected) Color.White else TextSecondary
                            )
                            Text(date.dayOfMonth.toString(),
                                fontSize = 18.sp, fontWeight = FontWeight.Bold,
                                color    = if (isSelected) Color.White else TextPrimary)
                            Box(modifier = Modifier.size(5.dp).background(
                                when {
                                    hasDot && isSelected -> Color.White
                                    hasDot               -> GreenDark
                                    else                 -> Color.Transparent
                                }, CircleShape))
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // Summary cards
            item {
                val totalKcal = selectedLogs.size * 150
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryCard(Modifier.weight(1f), YellowCard, iconRes = R.drawable.ic_care_food,
                        label = "Total Today", value = "$totalKcal", unit = "kcal")
                    SummaryCard(Modifier.weight(1f), PeachCard, emoji = "💧",
                        label = "Water Intake", value = "1.2", unit = "liters")
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                Text("Today's Log", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(8.dp))
            }

            if (selectedLogs.isNotEmpty()) {
                items(selectedLogs, key = { it.id }) { log ->
                    SwipeToDismissLogItem(
                        log      = log,
                        onDelete = { FeedingLogRepository.removeLog(context, dateKey, log.id) }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp,
                        modifier = Modifier.padding(start = 64.dp))
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                LogNextMealButton { showAddDialog = true }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showAddDialog) {
        AddFeedingLogDialog(
            onDismiss = { showAddDialog = false },
            onAdd     = { newLog ->
                FeedingLogRepository.addLog(context, dateKey, newLog)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissLogItem(log: FeedingLog, onDelete: () -> Unit) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange  = { if (it == SwipeToDismissBoxValue.EndToStart) { onDelete(); true } else false },
        positionalThreshold = { it * 0.35f }
    )
    SwipeToDismissBox(
        state = state, enableDismissFromStartToEnd = false,
        modifier = Modifier.fillMaxWidth(),
        backgroundContent = {
            Box(modifier = Modifier.fillMaxSize()
                .background(Color(0xFFFF4444)).padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🗑️", fontSize = 22.sp)
                    Text("Delete", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { FeedingLogItem(log) }
}

@Composable
private fun FeedingLogItem(log: FeedingLog) {
    val iconRes = runCatching { mealIconRes(log.type) }.getOrDefault(R.drawable.ic_care_food)
    Row(
        modifier          = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(44.dp).background(mealIconBg(log.type), CircleShape),
            contentAlignment = Alignment.Center) {
            Icon(painterResource(iconRes), log.type.label,
                tint = Color(0xFF5A3A00), modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(log.type.label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Row {
                Text(log.food, fontSize = 12.sp, color = TextSecondary)
                // amount: Int — показываем только если > 0
                if (log.amount > 0) {
                    Text(" • ${log.amount}g", fontSize = 12.sp,
                        color = GreenDark, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Text(log.time, fontSize = 12.sp, color = TextSecondary)
        Spacer(Modifier.width(6.dp))
        Icon(painterResource(R.drawable.ic_chevron_right), null,
            tint = Color(0xFFCCCCCC), modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun AddFeedingLogDialog(onDismiss: () -> Unit, onAdd: (FeedingLog) -> Unit) {
    var selectedType by remember { mutableStateOf(MealType.BREAKFAST) }
    var food         by remember { mutableStateOf("") }
    var amount       by remember { mutableStateOf("") }  // вводим как строку, конвертируем в Int
    val time = remember {
        java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Color.White,
        shape            = RoundedCornerShape(20.dp),
        title = { Text("Log Meal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Meal type", fontSize = 12.sp, color = TextSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(MealType.entries.toList()) { type ->
                        val isSel = selectedType == type
                        Box(modifier = Modifier.clip(RoundedCornerShape(50.dp))
                            .background(if (isSel) GreenDark else YellowCard)
                            .clickable { selectedType = type }
                            .padding(horizontal = 12.dp, vertical = 8.dp)) {
                            Text(type.label, fontSize = 12.sp,
                                color      = if (isSel) Color.White else TextSecondary,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
                OutlinedTextField(
                    value = food, onValueChange = { food = it },
                    label = { Text("Food (e.g. Dry Kibble)") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenDark, focusedLabelColor = GreenDark),
                    singleLine = true
                )
                OutlinedTextField(
                    value = amount, onValueChange = { amount = it.filter { c -> c.isDigit() } },
                    label = { Text("Amount in grams (e.g. 150)") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenDark, focusedLabelColor = GreenDark),
                    singleLine = true
                )
                Text("Time: $time", fontSize = 12.sp, color = TextSecondary)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (food.isNotBlank()) {
                        onAdd(FeedingLog(
                            id     = System.currentTimeMillis().toInt(),
                            type   = selectedType,
                            food   = food.trim(),
                            amount = amount.toIntOrNull() ?: 0,
                            time   = time
                        ))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                shape  = RoundedCornerShape(50.dp)
            ) { Text("Add", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}

@Composable private fun LogNextMealButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()
        .border(1.5.dp, Color(0xFFDDDDDD), RoundedCornerShape(16.dp))
        .clip(RoundedCornerShape(16.dp)).clickable { onClick() }.padding(vertical = 20.dp),
        contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(44.dp).background(GreenDark, CircleShape),
                contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.ic_add_circle), "Add",
                    tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text("Log Next Meal", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = GreenDark)
        }
    }
}

@Composable private fun SummaryCard(
    modifier: Modifier, bgColor: Color,
    iconRes: Int? = null, emoji: String? = null,
    label: String, value: String, unit: String
) {
    Column(modifier = modifier.background(bgColor, RoundedCornerShape(18.dp)).padding(16.dp)) {
        if (iconRes != null) Icon(painterResource(iconRes), null, tint = GreenDark, modifier = Modifier.size(22.dp))
        else if (emoji != null) Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = TextSecondary)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(" $unit", fontSize = 14.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 3.dp))
        }
    }
}

@Composable private fun MonthNavBtn(iconRes: Int, onClick: () -> Unit) {
    Box(modifier = Modifier.size(32.dp)
        .border(1.dp, Color(0xFFD0E8B0), RoundedCornerShape(50.dp))
        .clip(RoundedCornerShape(50.dp)).clickable { onClick() },
        contentAlignment = Alignment.Center) {
        Icon(painterResource(iconRes), null, tint = GreenDark, modifier = Modifier.size(16.dp))
    }
}