package com.example.catsinapp.debug

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// ─────────────────────────────────────────────────────────
// DEV MENU — появляется при встряхивании
// Вертикальная панель с двумя кнопками (Material3)
// ─────────────────────────────────────────────────────────

@Composable
fun DevMenuDialog(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    if (!visible) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            // Полупрозрачный фон (кликабельный для закрытия)
            // Панель — вертикальная, прижата к правому краю

            AnimatedVisibility(
                visible = visible,
                enter   = slideInHorizontally { it } + fadeIn(),
                exit    = slideOutHorizontally { it } + fadeOut()
            ) {
                DevMenuPanel(
                    onCollectLogs = {
                        LogCollector.collectAndShare(context)
                        onDismiss()
                    },
                    onCrash = {
                        onDismiss()
                        // Небольшая задержка чтобы диалог закрылся
                        throw RuntimeException("DEV CRASH — triggered manually by developer")
                    },
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
private fun DevMenuPanel(
    onCollectLogs: () -> Unit,
    onCrash: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(
                color = Color(0xFF1C1C1E),  // тёмный фон — iOS-like dark
                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
            )
            .padding(vertical = 32.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text       = "DEV MENU",
            color      = Color(0xFF8E8E93),
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(color = Color(0xFF3A3A3C))

        Spacer(Modifier.height(8.dp))

        // Кнопка 1 — Collect Logs
        DevMenuButton(
            icon        = "📋",
            label       = "Collect Logs",
            sublabel    = "Last 60 seconds",
            color       = Color(0xFF30D158),   // зелёный (системный iOS green)
            onClick     = onCollectLogs
        )

        // Кнопка 2 — Crash App
        DevMenuButton(
            icon     = "💥",
            label    = "Crash App",
            sublabel = "Force crash now",
            color    = Color(0xFFFF453A),      // красный (системный iOS red)
            onClick  = onCrash
        )

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(color = Color(0xFF3A3A3C))

        Spacer(Modifier.height(8.dp))

        // Кнопка закрыть
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text      = "Close",
                color     = Color(0xFF8E8E93),
                fontSize  = 14.sp
            )
        }

        // Hint
        Text(
            text      = "Shake to open/close",
            color     = Color(0xFF48484A),
            fontSize  = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DevMenuButton(
    icon: String,
    label: String,
    sublabel: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(72.dp),
        shape    = RoundedCornerShape(14.dp),
        colors   = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2C2C2E),
            contentColor   = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Иконка в цветном кружке
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 18.sp)
            }

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text       = label,
                    color      = Color.White,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text     = sublabel,
                    color    = color,
                    fontSize = 11.sp
                )
            }
        }
    }
}
