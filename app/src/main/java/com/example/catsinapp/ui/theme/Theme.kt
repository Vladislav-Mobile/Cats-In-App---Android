package com.example.catsinapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.GreenLight
import com.example.catsinapp.ui.theme.TextPrimary

private val LightColorScheme = lightColorScheme(
    primary        = GreenDark,
    onPrimary      = Color.White,
    secondary      = GreenLight,
    surface        = Color.White,
    background     = Color.White,
    onBackground   = TextPrimary,
    onSurface      = TextPrimary
)

@Composable
fun CatsInAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content     = content
    )
}
