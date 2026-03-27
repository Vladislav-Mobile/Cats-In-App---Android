package com.example.catsinapp.ui.weight

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*


enum class ActivityLevel(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}

@Composable
fun WeightEntryScreen(onBack: () -> Boolean) {
    var selectedUnit by remember { mutableStateOf("KG") }
    var selectedActivity by remember { mutableStateOf(ActivityLevel.LOW) }

    // Segmented Button KG / G
    SingleChoiceSegmentedButtonRow {
        listOf("KG", "G").forEachIndexed { i, unit ->
            SegmentedButton(
                selected = selectedUnit == unit,
                onClick  = { selectedUnit = unit },
                shape    = SegmentedButtonDefaults.itemShape(i, 2)
            ) { Text(unit) }
        }
    }

    // Activity chips
    Row {
        ActivityLevel.entries.forEach { level ->
            FilterChip(
                selected = selectedActivity == level,
                onClick  = { selectedActivity = level },
                label    = { Text(level.label) }
            )
        }
    }
}