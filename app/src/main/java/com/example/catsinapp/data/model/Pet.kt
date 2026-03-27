package com.example.catsinapp.data.model

enum class PetStatus {
    STAR_OF_WEEK,
    NEW_ARRIVAL,
    ACTIVE
}

data class Pet(
    val id: String,
    val name: String,
    val status: PetStatus,

    val imageRes: Int,     // или Int, см. ниже
    val subtitle: String,
    val secondChip: PetBadge? = null,
    val personalityText: String? = null,
    val vitalityStats: String? = null,
    val blockC: String? = null,
    val caregiverNote: String? = null,
    val ctaText: String = "",
    val badges: List<PetBadge> = emptyList()
)