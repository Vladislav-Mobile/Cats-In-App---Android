package com.example.catsinapp.data

import com.example.catsinapp.R
import com.example.catsinapp.data.model.*

object DataSource {

    fun getPets(): List<Pet> = listOf(

        // ── STARS OF THE WEEK ──────────────────────────────
        Pet(
            id = "oliver",
            name = "Oliver",
            subtitle = "Gentle giant, 2 years old • Indoor lover",
            imageRes = R.drawable.cat_oliver,
            status = PetStatus.STAR_OF_WEEK,
            badges = listOf(PetBadge.STAR_OF_WEEK),
            tags = listOf("Friendly with Peers", "High Social Interaction"),
            personalityText = "Gentle and calm, a purr machine lover.",
            vitalityStats = "Activity: 60% • Nutrition: 90% • Rest: 70%",
            ctaText = "Book a Meeting with Oliver"
        ),

        Pet(
            id = "luna",
            name = "Luna",
            subtitle = "Playful soul, 6 months • Very active",
            imageRes = R.drawable.cat_luna,
            status = PetStatus.STAR_OF_WEEK,
            badges = listOf(PetBadge.NEW_ARRIVAL),
            secondChip = Chip("High Energy"),
            tags = listOf("Loves Playtime"),
            personalityText = "Playful, curious, loves toys.",
            vitalityStats = "Energy: High",
            ctaText = "Meet Luna Today"
        ),

        Pet(
            id = "shadow",
            name = "Shadow",
            subtitle = "Quiet observer, 4 years old",
            imageRes = R.drawable.cat_shadow,
            status = PetStatus.STAR_OF_WEEK,
            badges = listOf(PetBadge.VET_CHECKED),
            secondChip = Chip("Tuxedo"),
            tags = listOf("Quiet Observer"),
            personalityText = "Prefers calm environments.",
            vitalityStats = "Activity: 80% • Rest: 85%"
        ),

        // ── NEW ARRIVALS ───────────────────────────────────
        Pet(
            id = "bella",
            name = "Bella",
            subtitle = "Sweet kitten",
            imageRes = R.drawable.cat_bella,
            status = PetStatus.NEW_ARRIVAL,
            badges = listOf(PetBadge.NEW_ARRIVAL),
            secondChip = Chip("Kitten"),
            tags = listOf("Very Social", "Curious"),
            ctaText = "Adopt Bella Today"
        ),

        Pet(
            id = "milo",
            name = "Milo",
            subtitle = "Calm and steady",
            imageRes = R.drawable.cat_milo,
            status = PetStatus.NEW_ARRIVAL,
            badges = listOf(PetBadge.NEW_ARRIVAL),
            secondChip = Chip("Adult"),
            tags = listOf("Independent"),
            vitalityStats = "Wellness: Excellent",
            ctaText = "Meet Milo Today"
        ),

        Pet(
            id = "charlie",
            name = "Charlie",
            subtitle = "Bundle of joy",
            imageRes = R.drawable.cat_charlie,
            status = PetStatus.NEW_ARRIVAL,
            badges = listOf(PetBadge.NEW_ARRIVAL),
            secondChip = Chip("Kitten"),
            tags = listOf("High Energy"),
            vitalityStats = "Vibrancy: 92%",
            ctaText = "Meet Charlie"
        ),

        // ── ACTIVE ─────────────────────────────────────────
        Pet(
            id = "felix",
            name = "Felix",
            subtitle = "Marathon Runner",
            imageRes = R.drawable.cat_felix,
            status = PetStatus.ACTIVE,
            badges = listOf(PetBadge.ACTIVE_NOW),
            vitalityStats = "Daily score: 84%",
            ctaText = "Start Activity"
        ),

        Pet(
            id = "oscar",
            name = "Oscar",
            subtitle = "Hyper Active",
            imageRes = R.drawable.cat_oscar,
            status = PetStatus.ACTIVE,
            badges = listOf(PetBadge.HYPER_ACTIVE),
            vitalityStats = "Energy: 95%",
            ctaText = "Adopt Oscar"
        ),

        Pet(
            id = "mochi",
            name = "Mochi",
            subtitle = "Very social",
            imageRes = R.drawable.cat_mochi,
            status = PetStatus.ACTIVE,
            badges = listOf(PetBadge.CLIMBER_SPECIALIST),
            secondChip = Chip("Active"),
            tags = listOf("Vocal", "Climber"),
            personalityText = "Athletic and playful.",
            ctaText = "Adopt Mochi"
        ),

        Pet(
            id = "ziggy",
            name = "Ziggy",
            subtitle = "Playful",
            imageRes = R.drawable.cat_ziggy,
            status = PetStatus.ACTIVE,
            badges = listOf(PetBadge.ACTIVE_CAT)
        ),

        Pet(
            id = "pepper",
            name = "Pepper",
            subtitle = "Spicy but sweet",
            imageRes = R.drawable.cat_pepper,
            status = PetStatus.ACTIVE,
            badges = listOf(PetBadge.ACTIVE_CAT),
            vitalityStats = "82% optimal"
        ),

        Pet(
            id = "lulu",
            name = "Lulu",
            subtitle = "Elegant",
            imageRes = R.drawable.cat_lulu,
            status = PetStatus.ACTIVE,
            badges = listOf(PetBadge.ACTIVE_CAT),
            secondChip = Chip("Quiet"),
            ctaText = "Meet Lulu"
        ),

        Pet(
            id = "toby",
            name = "Toby",
            subtitle = "Big boy",
            imageRes = R.drawable.cat_toby,
            status = PetStatus.ACTIVE,
            badges = listOf(PetBadge.ACTIVE_CAT, PetBadge.FRIENDLY),
            secondChip = Chip("Friendly"),
            vitalityStats = "92% vitals",
            caregiverNote = "Very social today",
            ctaText = "Sponsor Toby"
        ),

        Pet(
            id = "bean",
            name = "Bean",
            subtitle = "Tiny but mighty",
            imageRes = R.drawable.cat_bean,
            status = PetStatus.ACTIVE
        )
    )

    // ── Helpers ────────────────────────────────────────────
    fun getStarsOfTheWeek() = getPets().filter { it.status == PetStatus.STAR_OF_WEEK }
    fun getNewArrivals() = getPets().filter { it.status == PetStatus.NEW_ARRIVAL }
    fun getActiveCats() = getPets().filter { it.status == PetStatus.ACTIVE }
    fun getPetById(id: String) = getPets().firstOrNull { it.id == id }

    // ── TEMP: отключено (не соответствует моделям) ─────────
    fun getFeedingLogsState1(): List<FeedingLog> = emptyList()
    fun getFeedingLogsState2(): List<FeedingLog> = emptyList()
    fun getFeedingLogsEmpty(): List<FeedingLog> = emptyList()

    fun getPetProfile(): PetProfile? = null

    fun getCareCategories(): List<CareCategory> = emptyList()
}