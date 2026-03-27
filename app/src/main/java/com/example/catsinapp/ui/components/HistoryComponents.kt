package com.example.catsinapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.catsinapp.data.model.FeedingLog

@Composable
fun CalendarStrip() {
    Text("Calendar")
}

@Composable
fun SummaryCards() {
    Text("Summary")
}

@Composable
fun FeedingLogList(logs: List<FeedingLog>) {
    Text("Logs: ${logs.size}")
}

@Composable
fun LogNextMealButton() {
    Text("Add Meal")
}