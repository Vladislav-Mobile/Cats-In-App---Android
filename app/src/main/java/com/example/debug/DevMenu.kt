package com.example.catsinapp.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private val GreenDark = Color(0xFF2D5016)

@Composable
fun DevMenuDialog(visible: Boolean, onDismiss: () -> Unit) {
    if (!visible) return
    val context = LocalContext.current

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

            // Collect Logs
            DevMenuButton(label = "📋 Collect Logs") {
                LogCollector.collectAndShare(context)
                onDismiss()
            }

            // Memory Stats — новая кнопка
            DevMenuButton(label = "📊 Log Memory Stats") {
                AppLogger.logMemoryStats(context)
                onDismiss()
            }

            // Crash App
            DevMenuButton(
                label    = "💥 Crash App",
                color    = Color(0xFFFF4444)
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