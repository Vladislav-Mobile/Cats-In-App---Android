package com.example.catsinapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeding_logs")
data class FeedingLogEntity(
    @PrimaryKey val id: Int,
    val dateKey: String,   // формат YYYY-MM-DD
    val type: String,      // MealType.name
    val food: String,
    val amount: Int,
    val time: String
)
