package com.example.catsinapp.ui.weight

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catsinapp.R
import com.example.catsinapp.data.model.ActivityLevel
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.GreenLight
import com.example.catsinapp.ui.theme.GreenPaleBg
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

private val YellowCard = Color(0xFFF5F7D0)

// Список уровней активности — явно перечисляем чтобы не зависеть от .entries
private val activityLevels = listOf(
    ActivityLevel.LOW,
    ActivityLevel.MODERATE,
    ActivityLevel.HIGH,
    ActivityLevel.WORKOUT
)

@Composable
fun WeightEntryScreen(onBack: () -> Unit) {
    var selectedUnit     by remember { mutableStateOf("KG") }
    var selectedActivity by remember { mutableStateOf(ActivityLevel.LOW) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ── Toolbar ───────────────────────────────────────
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter            = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint               = GreenDark,
                modifier           = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text       = "Mobile app from Vlad Kazachek",
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = GreenDark,
                modifier   = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) { Text("▶", color = Color.White, fontSize = 12.sp) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Аватар с кнопкой камеры ───────────────────
            Box(
                modifier         = Modifier.size(110.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(YellowCard),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter            = painterResource(R.drawable.cat_buddy_profile),
                        contentDescription = "Buddy",
                        contentScale       = ContentScale.Fit,
                        modifier           = Modifier.fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5D4037)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter            = painterResource(R.drawable.ic_camera),
                        contentDescription = "Camera",
                        tint               = Color.White,
                        modifier           = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text("Pet Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Update your buddy's physical details", fontSize = 13.sp, color = TextSecondary)

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Name ──────────────────────────────────
                FieldLabel("Name")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(YellowCard, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text("Buddy", fontSize = 16.sp, color = TextPrimary)
                }

                // ── Weight toggle KG / G ──────────────────
                FieldLabel("Weight Measurement")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(YellowCard, RoundedCornerShape(16.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("KG", "G").forEach { unit ->
                        val isSelected = selectedUnit == unit
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) GreenDark else Color.Transparent)
                                .clickable { selectedUnit = unit },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = unit,
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color      = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }

                // ── Current Weight ────────────────────────
                FieldLabel("Current Weight")
                Box(
                    modifier         = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier          = Modifier
                            .background(Color(0xFFF8F8F8), RoundedCornerShape(16.dp))
                            .padding(horizontal = 24.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text       = "12.5",
                            fontSize   = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color      = TextPrimary
                        )
                        Text(
                            text     = " ${selectedUnit.lowercase()}",
                            fontSize = 16.sp,
                            color    = TextSecondary,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }

                // ── Activity Level chips ──────────────────
                FieldLabel("Activity Level")
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    activityLevels.forEach { level ->
                        val isSelected = selectedActivity == level
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(
                                    if (isSelected) GreenLight else GreenPaleBg
                                )
                                .clickable { selectedActivity = level }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text       = level.label,
                                fontSize   = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color      = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ── Save button ───────────────────────────
                Button(
                    onClick  = { onBack() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(26.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = GreenDark)
                ) {
                    Text(
                        text       = "💾  Save Profile",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color.White
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FieldLabel(label: String) {
    Text(text = label, fontSize = 13.sp, color = TextSecondary)
}