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

    val imageRes: Int,
    val subtitle: String,

    val personalityText: String? = null,
    val vitalityStats: String? = null,
    val blockC: String? = null,
    val caregiverNote: String? = null,
    val ctaText: String = "",

    val badges: List<PetBadge> = emptyList(),
    val secondChip: Chip? = null,
    val tags: List<String> = emptyList()
)
data class Chip(
    val value: String
)