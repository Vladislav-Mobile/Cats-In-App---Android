package com.example.catsinapp.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.ui.components.CalendarStrip
import com.example.catsinapp.ui.components.FeedingLogList
import com.example.catsinapp.ui.components.LogNextMealButton
import com.example.catsinapp.ui.components.SummaryCards


@Composable
fun HistoryScreen() {
    var stateIndex by remember { mutableStateOf(0) }
    val logs = when (stateIndex) {
        0 -> DataSource.getFeedingLogsState1()
        1 -> DataSource.getFeedingLogsState2()
        else -> DataSource.getFeedingLogsEmpty()
    }

    Column {
        CalendarStrip()
        SummaryCards()
        FeedingLogList(logs)
        LogNextMealButton()
    }
}