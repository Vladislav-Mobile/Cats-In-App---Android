package com.example.catsinapp.data.model

// ─── Статус кота ──────────────────────────────────────────
enum class PetStatus {
    STAR_OF_WEEK,
    NEW_ARRIVAL,
    ACTIVE
}

// ─── Основная модель ──────────────────────────────────────
data class Pet(
    val id: String,
    val name: String,
    val status: PetStatus,
    val imageRes: Int,
    val subtitle: String,

    // Детальный экран
    val arrivalText: String = "",
    val personalityText: String? = null,
    val aboutText: String? = null,
    val storyQuote: String? = null,
    val vitalityStats: VitalityStats? = null,
    val blockC: PetBlockC? = null,
    val caregiverNote: CaregiverNote? = null,
    val ctaText: String = "",
    val ctaSecondary: String? = null,

    // Карточки
    val badges: List<PetBadge> = emptyList(),
    val secondChip: Chip? = null,
    val tags: List<String> = emptyList()
)

// ─── Чип (Arrival, Temperament и т.д.) ───────────────────
data class Chip(
    val label: String = "",
    val value: String
)

// ─── Статистика здоровья ──────────────────────────────────
data class VitalityStats(
    val title: String = "",
    val percentLabel: String? = null,
    val activity: StatItem? = null,
    val nutrition: StatItem? = null,
    val rest: StatItem? = null,
    val social: StatItem? = null,
    val energyLevel: StatItem? = null,
    val socialNeeds: StatItem? = null
)

data class StatItem(
    val label: String,
    val value: String
)

// ─── Блок C (Daily Vibe / Core Traits / etc) ─────────────
data class PetBlockC(
    val type: BlockCType,
    val title: String = "",
    val items: List<BlockCItem> = emptyList(),
    val text: String? = null
)

enum class BlockCType {
    DAILY_VIBE,
    CORE_TRAITS,
    INFO_CARDS,
    AWARDS,
    HEALTH_LOG,
    NEXT_RUN
}

data class BlockCItem(
    val title: String,
    val description: String,
    val highlighted: Boolean = false
)

// ─── Заметка смотрителя ───────────────────────────────────
data class CaregiverNote(
    val caregiverName: String? = null,
    val text: String,
    val timeAgo: String? = null
)