package com.example.catsinapp.data.model

data class FeedingLog(
    val id: Int,
    val type: MealType,
    val food: String,
    val amount: Int,
    val time: String
)

enum class MealType(val label: String) {
    BREAKFAST("Breakfast"),
    MORNING_TREAT("Morning Treat"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack")
}