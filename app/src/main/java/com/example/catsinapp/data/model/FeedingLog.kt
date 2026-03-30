package com.example.catsinapp.data.model

enum class MealType {
    BREAKFAST,
    MORNING_TREAT,
    LUNCH,
    DINNER,
    SNACK
}

data class FeedingLog(
    val id: String,
    val type: MealType,
    val food: String,
    val amount: String,
    val time: String
)