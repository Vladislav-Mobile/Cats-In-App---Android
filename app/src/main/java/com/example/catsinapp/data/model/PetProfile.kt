package com.example.catsinapp.data.model

data class PetProfile(
    val name: String = "Buddy",
    val birthDate: String = "05/12/2021",
    val weightKg: Float = 12f,
    val weightG: Int = 450,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val activityLevel: ActivityLevel = ActivityLevel.LOW
)

enum class WeightUnit { KG, G }

enum class ActivityLevel(val label: String) {
    LOW("Low"),
    MODERATE("Moderate"),
    HIGH("High"),
    WORKOUT("Workout")
}