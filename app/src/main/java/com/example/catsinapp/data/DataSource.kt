package com.example.catsinapp.data

import com.example.catsinapp.R
import com.example.catsinapp.data.model.*
import com.example.catsinapp.data.model.CareExtraBlock
import com.example.catsinapp.data.model.TableRow
import com.example.catsinapp.data.model.NumberedItem

object DataSource {

    fun getPets(): List<Pet> = listOf(

        // ── STARS OF THE WEEK ──────────────────────────────

        Pet(
            id          = "oliver",
            name        = "Oliver",
            subtitle    = "Gentle giant, 2 years old • Indoor lover",
            imageRes    = R.drawable.cat_oliver,
            status      = PetStatus.STAR_OF_WEEK,
            badges      = listOf(PetBadge.STAR_OF_WEEK),
            arrivalText = "2 years ago",
            tags        = listOf("Friendly with Peers", "High Social Interaction"),
            personalityText = "Gentle and calm, a purr machine lover. Oliver finds the soul in every silence.",
            vitalityStats = VitalityStats(
                title     = "Vitality Summary",
                activity  = StatItem("Activity (Moderate)", "60%"),
                nutrition = StatItem("Nutrition (Excellent)", "90%"),
                rest      = StatItem("Social Rest", "70%")
            ),
            blockC = PetBlockC(
                type  = BlockCType.DAILY_VIBE,
                title = "Personality details",
                items = listOf(
                    BlockCItem("Affectionate", "Loves being petted and curling up on laps."),
                    BlockCItem("Purr Machine", "Vibrates with joy when he sees his favorite humans.")
                )
            ),
            ctaText = "Book a Meeting with Oliver"
        ),

        Pet(
            id          = "luna",
            name        = "Luna",
            subtitle    = "Playful soul, 6 months • Very active",
            imageRes    = R.drawable.cat_luna,
            status      = PetStatus.STAR_OF_WEEK,
            badges      = listOf(PetBadge.NEW_ARRIVAL),
            arrivalText = "3 months ago",
            secondChip  = Chip(label = "ENERGY LEVEL", value = "High Energy"),
            tags        = listOf("Loves Playtime"),
            personalityText = "Playful, curious, loves toys, and exceptionally active. Luna turns every corner into a space adventure.",
            aboutText   = "Luna arrived at our sanctuary three months ago and immediately became the center of gravity.",
            vitalityStats = VitalityStats(
                title     = "Vitality Summary",
                activity  = StatItem("Activity (Moderate)", "60%"),
                nutrition = StatItem("Nutrition (Excellent)", "90%"),
                rest      = StatItem("Social Rest", "70%")
            ),
            ctaText = "Meet Luna Today"
        ),

        Pet(
            id          = "shadow",
            name        = "Shadow",
            subtitle    = "Quiet observer, 4 years old • loves scratches",
            imageRes    = R.drawable.cat_shadow,
            status      = PetStatus.STAR_OF_WEEK,
            badges      = listOf(PetBadge.VET_CHECKED),
            arrivalText = "1 year ago",
            secondChip  = Chip(label = "BREED", value = "Tuxedo"),
            tags        = listOf("Quiet Observer", "Selective Socializer"),
            personalityText = "Shadow is the ultimate quiet observer. He prefers to watch from sun-drenched corners.",
            vitalityStats = VitalityStats(
                title     = "VITALITY STATS",
                activity  = StatItem("ACTIVITY", "80%"),
                nutrition = StatItem("NUTRITION", "65%"),
                rest      = StatItem("REST", "85%")
            ),
            blockC = PetBlockC(
                type  = BlockCType.DAILY_VIBE,
                title = "Daily Rituals",
                text  = "As a professional scratcher, Shadow begins his day with a vigorous morning stretch against his favorite sisal post."
            )
        ),

        // ── NEW ARRIVALS ───────────────────────────────────

        Pet(
            id          = "bella",
            name        = "Bella",
            subtitle    = "Sweet kitten who brings instant joy.",
            imageRes    = R.drawable.cat_bella,
            status      = PetStatus.NEW_ARRIVAL,
            badges      = listOf(PetBadge.NEW_ARRIVAL),
            arrivalText = "2 weeks ago",
            secondChip  = Chip(label = "STATUS", value = "Kitten"),
            tags        = listOf("Very Social", "Curious", "Loves Kittens"),
            storyQuote  = "\"Sweet kitten who brings instant joy. Bella is the latest bundle of happiness in our sanctuary, ready to find her forever home.\"",
            blockC = PetBlockC(
                type  = BlockCType.INFO_CARDS,
                items = listOf(
                    BlockCItem("Very Social", "Loves being the center of attention and meeting new friends."),
                    BlockCItem("Curious", "Always the first to investigate a new box or a moving toy."),
                    BlockCItem("Loves Kittens", "Perfect for a multi-cat household.")
                )
            ),
            ctaText = "Adopt Bella Today"
        ),

        Pet(
            id          = "milo",
            name        = "Milo",
            subtitle    = "Calm and steady energy.",
            imageRes    = R.drawable.cat_milo,
            status      = PetStatus.NEW_ARRIVAL,
            badges      = listOf(PetBadge.NEW_ARRIVAL),
            arrivalText = "6 months ago",
            secondChip  = Chip(label = "STATUS", value = "Adult"),
            tags        = listOf("Independent", "Social with Cats"),
            aboutText   = "\"Calm and steady energy.\"",
            vitalityStats = VitalityStats(
                title     = "Wellness Snapshot",
                activity  = StatItem("Activity", "Optimal"),
                nutrition = StatItem("Nutrition", "Excellent"),
                rest      = StatItem("Rest", "Perfect")
            ),
            ctaText = "Meet Milo Today"
        ),

        Pet(
            id          = "charlie",
            name        = "Charlie",
            subtitle    = "Bundle of joy looking for a forever home.",
            imageRes    = R.drawable.cat_charlie,
            status      = PetStatus.NEW_ARRIVAL,
            badges      = listOf(PetBadge.NEW_ARRIVAL),
            arrivalText = "1 month ago",
            secondChip  = Chip(label = "STATUS", value = "Kitten"),
            tags        = listOf("High Energy", "Loves Wrestling"),
            vitalityStats = VitalityStats(
                title        = "Health Snapshot",
                percentLabel = "92% VIBRANCY",
                activity     = StatItem("Activity", "Peak Performance"),
                nutrition    = StatItem("Diet", "Growing Strong")
            ),
            blockC = PetBlockC(
                type  = BlockCType.INFO_CARDS,
                title = "Adopt Charlie",
                text  = "Join 12 other families interested in this bundle of joy."
            ),
            ctaText      = "Meet Charlie",
            ctaSecondary = "Save to Favorites"
        ),

        // ── ACTIVE CATS ────────────────────────────────────

        Pet(
            id          = "felix",
            name        = "Felix",
            subtitle    = "Marathon Runner",
            imageRes    = R.drawable.cat_felix,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.ACTIVE_NOW, PetBadge.MARATHON_SPIRIT),
            arrivalText = "8 months ago",
            tags        = listOf("Energetic & Spirited"),
            vitalityStats = VitalityStats(
                title        = "Vitals Tracking",
                percentLabel = "84% DAILY SCORE",
                activity     = StatItem("Activity", ""),
                nutrition    = StatItem("Nutrition", ""),
                rest         = StatItem("Rest", "")
            ),
            blockC = PetBlockC(
                type  = BlockCType.CORE_TRAITS,
                title = "Core Traits",
                items = listOf(
                    BlockCItem("Endurance", "Never tires of the chase."),
                    BlockCItem("Gregarious", "Loves being the center of attention.")
                )
            ),
            caregiverNote = CaregiverNote(
                text    = "Scheduled for 5:30 PM today in the back garden.",
                timeAgo = "Next Run"
            ),
            ctaText = "Start Activity"
        ),

        Pet(
            id          = "oscar",
            name        = "Oscar",
            subtitle    = "\"The Speedster of the Sanctuary\"",
            imageRes    = R.drawable.cat_oscar,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.HYPER_ACTIVE, PetBadge.ONE_YEAR_OLD),
            arrivalText = "4 months ago",
            vitalityStats = VitalityStats(
                title       = "Vitality Stats",
                energyLevel = StatItem("ENERGY LEVEL", "95%"),
                socialNeeds = StatItem("SOCIAL NEEDS", "80%")
            ),
            blockC = PetBlockC(
                type  = BlockCType.INFO_CARDS,
                items = listOf(
                    BlockCItem("Interactive Play", "Constant play required. Oscar loves toys that challenge his speed."),
                    BlockCItem("Social Butterfly", "Thrives with an equally energetic feline partner."),
                    BlockCItem("Shelter Story", "Arrived 4 months ago from a rescue center.")
                )
            ),
            ctaText = "Adopt Oscar"
        ),

        Pet(
            id          = "mochi",
            name        = "Mochi",
            subtitle    = "Loves climbing everything in sight. Very social and vocal.",
            imageRes    = R.drawable.cat_mochi,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.CLIMBER_SPECIALIST),
            arrivalText = "7 months ago",
            secondChip  = Chip(label = "HEALTH", value = "Vocal & Active"),
            tags        = listOf("Vocal communicator", "Very social", "Climber"),
            personalityText = "Mochi is our resident athlete. She loves climbing everything from high shelves to scratching posts.",
            ctaText = "Adopt Mochi"
        ),

        Pet(
            id          = "ziggy",
            name        = "Ziggy",
            subtitle    = "Enthusiastic player who brings a whirlwind of energy.",
            imageRes    = R.drawable.cat_ziggy,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.ACTIVE_CAT),
            arrivalText = "3 months ago",
            tags        = listOf("Playful"),
            blockC = PetBlockC(
                type  = BlockCType.DAILY_VIBE,
                title = "Personality",
                text  = "Playful"
            )
        ),

        Pet(
            id          = "pepper",
            name        = "Pepper",
            subtitle    = "Spicy but sweet",
            imageRes    = R.drawable.cat_pepper,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.ACTIVE_CAT),
            arrivalText = "5 months ago",
            tags        = listOf("'Sassy'", "'Loves Cuddles'"),
            vitalityStats = VitalityStats(
                title        = "Daily Vitality",
                percentLabel = "82% OPTIMAL"
            ),
            caregiverNote = CaregiverNote(
                text = "Pepper exhibits a unique blend of independent curiosity and deep affection. She thrives on high-energy play followed by intense cuddle sessions."
            )
        ),

        Pet(
            id          = "lulu",
            name        = "Lulu",
            subtitle    = "Elegant and calm. Her presence is quiet but deeply felt.",
            imageRes    = R.drawable.cat_lulu,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.ACTIVE_CAT),
            arrivalText = "2 months ago",
            secondChip  = Chip(label = "TEMPERAMENT", value = "Quiet"),
            blockC = PetBlockC(
                type  = BlockCType.DAILY_VIBE,
                title = "Daily Vibe",
                items = listOf(
                    BlockCItem("Morning Routine", "Soft stretches by the window at dawn, followed by a silent watch over the garden birds."),
                    BlockCItem("Social Style", "Preferring one-on-one time, Lulu will gently place her paw on your arm when she wants affection."),
                    BlockCItem("Zen Moments", "Known to spend hours in deep meditation on the highest shelf of her favorite cat tree.", highlighted = true)
                )
            ),
            ctaText = "Meet Lulu"
        ),

        Pet(
            id          = "toby",
            name        = "Toby",
            subtitle    = "Big boy with a big heart.",
            imageRes    = R.drawable.cat_toby,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.ACTIVE_CAT, PetBadge.FRIENDLY),
            arrivalText = "4 months ago",
            secondChip  = Chip(label = "PERSONALITY", value = "Friendly"),
            vitalityStats = VitalityStats(
                title        = "Health Summary",
                percentLabel = "92% VITALS"
            ),
            blockC = PetBlockC(
                type  = BlockCType.DAILY_VIBE,
                title = "'Loves Nap Time'",
                text  = "Toby's favorite activity is finding the warmest sunbeam and curling up for a long afternoon snooze."
            ),
            caregiverNote = CaregiverNote(
                caregiverName = "Anna Mitchell",
                text          = "Toby has been very social today! He greeted everyone at the door and even let a new volunteer brush his coat for 20 minutes.",
                timeAgo       = "2 days ago"
            ),
            ctaText = "Sponsor Toby"
        ),

        Pet(
            id          = "bean",
            name        = "Bean",
            subtitle    = "Tiny but mighty. The smallest runner in the house.",
            imageRes    = R.drawable.cat_bean,
            status      = PetStatus.ACTIVE,
            badges      = listOf(PetBadge.ACTIVE_CAT),
            arrivalText = "5 months ago",
            blockC = PetBlockC(
                type  = BlockCType.AWARDS,
                items = listOf(
                    BlockCItem("Brave Heart", "Recognition for exceptional courage during the initial transition period. A heart larger than life."),
                    BlockCItem("Smallest Runner", "Consistency Award: 2.4km daily sprint average.")
                )
            ),
            caregiverNote = CaregiverNote(
                text = "Routine Checkup: Completed 2 weeks ago\nDietary Plan: Premium High-Protein Mix"
            )
        )
    )

    // ── Helpers ────────────────────────────────────────────
    fun getStarsOfTheWeek() = getPets().filter { it.status == PetStatus.STAR_OF_WEEK }
    fun getNewArrivals()    = getPets().filter { it.status == PetStatus.NEW_ARRIVAL }
    fun getActiveCats()     = getPets().filter { it.status == PetStatus.ACTIVE }
    fun getPetById(id: String) = getPets().firstOrNull { it.id == id }

    fun getFeedingLogsState1(): List<FeedingLog> = emptyList()
    fun getFeedingLogsState2(): List<FeedingLog> = emptyList()
    fun getFeedingLogsEmpty(): List<FeedingLog>  = emptyList()

    fun getPetProfile(): PetProfile? = null

    fun getCareCategories(): List<CareCategory> = listOf(

        CareCategory(
            id          = "walks",
            title       = "Walks",
            iconRes     = R.drawable.ic_care_walks,
            description = "Regular physical activity is essential for maintaining your pet's physical and mental health. Without enough exercise, pets may develop anxiety, excess energy, or weight issues.\n\nDaily walks support heart health, strengthen joints, and provide mental stimulation through new environments and scents. The ideal duration depends on your pet's size and energy level.\n\nMaintain a consistent routine and adjust intensity based on your pet's condition.",
            extraBlock  = CareExtraBlock.Table(
                title = "Daily Duration Guide",
                rows  = listOf(
                    TableRow("Small Breeds",  null, "20–30 min"),
                    TableRow("Medium Breeds", null, "45–60 min"),
                    TableRow("Large Breeds",  null, "60–90 min")
                )
            )
        ),

        CareCategory(
            id          = "care",
            title       = "Care",
            iconRes     = R.drawable.ic_care_care,
            description = "Regular grooming supports healthy skin and coat while reducing shedding and tangles.\n\nAdjust grooming frequency based on coat type. Use proper tools and be gentle to avoid discomfort.\n\nWatch for any unusual changes in the skin or fur, as they may indicate health issues."
        ),

        CareCategory(
            id          = "food",
            title       = "Food",
            iconRes     = R.drawable.ic_care_food,
            description = "Regular physical activity is essential for maintaining your pet's physical and mental health. Without enough exercise, pets may develop anxiety, excess energy, or weight issues.\n\nDaily walks support heart health, strengthen joints, and provide mental stimulation through new environments and scents. The ideal duration depends on your pet's size and energy level.\n\nMaintain a consistent routine and adjust intensity based on your pet's condition.",
            extraBlock  = CareExtraBlock.Table(
                title     = "",
                hasHeader = true,
                rows      = listOf(
                    TableRow("Dry Kibble",   "Twice Daily",  "50g"),
                    TableRow("Wet Topper",  "Evening Only", "60g"),
                    TableRow("Dental Chew", "Post-Walk",    "1 Stick")
                )
            )
        ),

        CareCategory(
            id          = "health",
            title       = "Health",
            iconRes     = R.drawable.ic_care_health,
            description = "Pets cannot communicate discomfort directly, so it's important to monitor changes in their behavior and condition. Watch for shifts in appetite, energy levels, breathing, or water intake. Tracking these regularly helps detect potential issues early.",
            extraBlock  = CareExtraBlock.NumberedList(
                items = listOf(
                    NumberedItem(1, "Basic Care",       "A healthy cat needs good food, clean water, and regular vet check-ups."),
                    NumberedItem(2, "Signs of Illness", "Watch for behavior changes: low energy, loss of appetite, hiding, or unusual affection. Vomiting or discharge are also warning signs."),
                    NumberedItem(3, "Nutrition",        "Poor food can cause serious health issues."),
                    NumberedItem(4, "Water",            "Cats often drink too little. Use fountains, wet food, or add water to meals to support kidney health."),
                    NumberedItem(5, "Activity",         "Daily play (10–15 minutes) helps prevent obesity, diabetes, and joint problems.")
                )
            )
        ),

        CareCategory(
            id          = "veterinarians",
            title       = "Veterinarians",
            iconRes     = R.drawable.ic_care_vet,
            description = "Regular veterinary visits help detect health issues early, even before visible symptoms appear.\n\nFollow vaccination schedules and routine check-ups. Younger and older pets may require more frequent visits. Discuss any changes in behavior or diet with your vet.\n\nPreventive care is more effective and less stressful than treating advanced conditions."
        ),

        CareCategory(
            id          = "toys",
            title       = "Toys",
            iconRes     = R.drawable.ic_care_toys,
            description = "Toys are important for both physical activity and mental stimulation.\n\nProvide a variety of toys to prevent boredom and rotate them regularly to maintain interest. Choose safe, durable options suitable for your pet's size.\n\nAvoid damaged toys, as they may become a safety risk.",
            imageRes    = R.drawable.img_care_toys
        ),

        CareCategory(
            id          = "space",
            title       = "Space",
            iconRes     = R.drawable.ic_care_space,
            description = "A safe and organized environment helps your pet feel secure and prevents accidents.\n\nRemove hazards such as loose wires, toxic plants, or small objects that can be swallowed. Keep important items out of reach.\n\nCreate a dedicated space where your pet can relax and feel comfortable.",
            imageRes    = R.drawable.img_care_space
        ),

        CareCategory(
            id          = "grooming",
            title       = "Grooming",
            iconRes     = R.drawable.ic_care_grooming,
            description = "Regular grooming supports healthy skin and coat while reducing shedding and tangles.\n\nAdjust grooming frequency based on coat type. Use proper tools and be gentle to avoid discomfort.\n\nWatch for any unusual changes in the skin or fur, as they may indicate health issues.",
            imageRes    = R.drawable.img_care_grooming
        ),

        CareCategory(
            id          = "bathing",
            title       = "Bathing",
            iconRes     = R.drawable.ic_care_bathing,
            description = "Bathing helps keep your pet's skin and coat clean, but it should be done carefully to avoid stress or irritation.\n\nUse lukewarm water and pet-safe shampoo. Avoid washing too frequently, as it can dry out the skin. Always protect the eyes and ears during the process.\n\nDry your pet thoroughly and reward them afterward to create a calm, positive experience.",
            imageRes    = R.drawable.img_care_bathing
        ),

        CareCategory(
            id          = "vaccination",
            title       = "Vaccination",
            iconRes     = R.drawable.ic_care_vaccination,
            description = "Regular veterinary visits are key to preventive care and early diagnosis.\n\nSchedule routine check-ups and discuss any concerns about behavior, diet, or health. Early detection helps avoid serious complications.\n\nBuilding a relationship with a trusted veterinarian ensures better long-term care for your pet.",
            imageRes    = R.drawable.img_care_vaccination
        ),

        CareCategory(
            id          = "sleep",
            title       = "Sleap",
            iconRes     = R.drawable.ic_care_sleep,
            description = "Quality sleep is essential for your pet's recovery, mood, and overall health.\n\nEnsure your pet has a quiet, comfortable place to rest. Avoid disturbing them during sleep, especially after active periods.\n\nChanges in sleep patterns may indicate stress or health concerns and should be monitored.",
            imageRes    = R.drawable.img_care_sleep
        ),

        CareCategory(
            id          = "game",
            title       = "Game",
            iconRes     = R.drawable.ic_care_game,
            description = "Play is a key part of your pet's daily routine. It helps release energy, prevent boredom, and improve behavior.\n\nUse a mix of physical and mental games to keep your pet engaged. Short, regular sessions are more effective than occasional long ones.\n\nInteractive play also strengthens your bond and builds trust.",
            imageRes    = R.drawable.img_care_game
        )
    )
}