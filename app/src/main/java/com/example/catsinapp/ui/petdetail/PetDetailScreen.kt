package com.example.catsinapp.ui.petdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catsinapp.R
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.data.model.*
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.GreenLight
import com.example.catsinapp.ui.theme.GreenPale
import com.example.catsinapp.ui.theme.GreenPaleBg
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

// Локальные цвета
private val YellowCard = Color(0xFFF5F7D0)
private val PeachCard  = Color(0xFFFDDCCC)
private val AmberText  = Color(0xFF8B6914)

@Composable
fun PetDetailScreen(petId: String, onBack: () -> Unit) {
    val pet = DataSource.getPetById(petId) ?: return

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item { HeroSection(pet = pet, onBack = onBack) }

            // Имя + subtitle (кроме Luna — у неё overlay)
            if (pet.id != "luna") {
                item { NameSection(pet = pet) }
            }

            // Info chips
            if (pet.arrivalText.isNotBlank() || pet.secondChip != null) {
                item { InfoChipsSection(pet = pet) }
            }

            // Tags
            if (pet.tags.isNotEmpty()) {
                item { TagsSection(tags = pet.tags) }
            }

            // Personality
            pet.personalityText?.let { text ->
                item { PersonalityCard(title = "Personality", text = text) }
            }

            // About
            pet.aboutText?.let { text ->
                item { AboutSection(petName = pet.name, text = text) }
            }

            // Story quote
            pet.storyQuote?.let { quote ->
                item { StorySection(quote = quote) }
            }

            // Vitality Stats
            pet.vitalityStats?.let { stats ->
                item { VitalityStatsSection(stats = stats) }
            }

            // Block C
            pet.blockC?.let { block ->
                item { BlockCSection(block = block) }
            }

            // Caregiver Note
            pet.caregiverNote?.let { note ->
                item { CaregiverSection(note = note) }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }

        // Sticky CTA кнопка
        if (pet.ctaText.isNotBlank()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick  = {},
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(26.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = GreenDark)
                ) {
                    Text(pet.ctaText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                pet.ctaSecondary?.let { secondary ->
                    OutlinedButton(
                        onClick  = {},
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(26.dp),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = GreenDark)
                    ) {
                        Text(secondary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// HERO — фото + кнопка назад + badges
// ─────────────────────────────────────────────────────────
@Composable
private fun HeroSection(pet: Pet, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Image(
            painter            = painterResource(id = pet.imageRes),
            contentDescription = pet.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        )

        // Градиент + имя поверх (только Luna)
        if (pet.id == "luna") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .align(Alignment.BottomCenter)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.7f))))
            )
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                Text(pet.name, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                if (pet.arrivalText.isNotBlank()) {
                    Text(
                        text     = "Arrival ${pet.arrivalText}",
                        color    = Color.White.copy(0.85f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Кнопка назад
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .background(Color.White.copy(0.9f), RoundedCornerShape(12.dp))
                .clickable { onBack() }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter            = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back",
                tint               = GreenDark,
                modifier           = Modifier.size(18.dp)
            )
            Text("Cat Profile", color = GreenDark, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }

        // Vet Checked badge
        if (pet.badges.contains(PetBadge.VET_CHECKED)) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("✓", fontSize = 10.sp, color = GreenDark, fontWeight = FontWeight.Bold)
                Text("Vet Checked", fontSize = 11.sp, color = GreenDark, fontWeight = FontWeight.SemiBold)
            }
        }

        // Статусные бейджи вверху (Star of Week, New Arrival, Hyper Active)
        val topBadges = pet.badges.filter {
            it != PetBadge.VET_CHECKED && it != PetBadge.ACTIVE_CAT && it != PetBadge.ACTIVE_NOW
        }.take(2)

        if (topBadges.isNotEmpty() && pet.id != "luna") {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 56.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                topBadges.forEach { badge ->
                    val (bg, fg) = badgeColors(badge)
                    Box(
                        modifier = Modifier
                            .background(bg, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(badge.label.uppercase(), color = fg, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// NAME SECTION
// ─────────────────────────────────────────────────────────
@Composable
private fun NameSection(pet: Pet) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        val statusBadge = pet.badges.firstOrNull {
            it == PetBadge.STAR_OF_WEEK || it == PetBadge.ACTIVE_CAT || it == PetBadge.NEW_ARRIVAL
        }
        statusBadge?.let {
            Text(
                text          = it.label.uppercase(),
                fontSize      = 11.sp,
                color         = if (it == PetBadge.STAR_OF_WEEK) Color(0xFFCC7722) else GreenDark,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
        Text(pet.name, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(pet.subtitle, fontSize = 15.sp, color = TextSecondary)
    }
}

// ─────────────────────────────────────────────────────────
// INFO CHIPS — Arrival + secondChip
// ─────────────────────────────────────────────────────────
@Composable
private fun InfoChipsSection(pet: Pet) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (pet.arrivalText.isNotBlank()) {
            InfoChipCard(
                label    = "ARRIVAL",
                value    = pet.arrivalText,
                modifier = Modifier.weight(1f)
            )
        }
        pet.secondChip?.let { chip ->
            InfoChipCard(
                label    = chip.label.ifBlank { "INFO" },
                value    = chip.value,
                modifier = Modifier.weight(1f),
                bgColor  = PeachCard
            )
        }
    }
}

@Composable
private fun InfoChipCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    bgColor: Color = YellowCard
) {
    Column(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Text(label, fontSize = 10.sp, color = AmberText, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Text(value, fontSize = 16.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
    }
}

// ─────────────────────────────────────────────────────────
// TAGS
// ─────────────────────────────────────────────────────────
@Composable
private fun TagsSection(tags: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.take(3).forEach { tag ->
            Box(
                modifier = Modifier
                    .background(GreenPale, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(tag, fontSize = 12.sp, color = GreenDark, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// PERSONALITY CARD
// ─────────────────────────────────────────────────────────
@Composable
private fun PersonalityCard(title: String, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(YellowCard, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(Modifier.height(6.dp))
        Text(text, fontSize = 14.sp, color = TextPrimary, lineHeight = 20.sp)
    }
}

// ─────────────────────────────────────────────────────────
// ABOUT SECTION
// ─────────────────────────────────────────────────────────
@Composable
private fun AboutSection(petName: String, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(YellowCard, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { Text("📄", fontSize = 16.sp) }
            Text("About $petName", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        Text(text, fontSize = 14.sp, color = TextSecondary, lineHeight = 20.sp)
    }
}

// ─────────────────────────────────────────────────────────
// STORY SECTION — цитата (Bella)
// ─────────────────────────────────────────────────────────
@Composable
private fun StorySection(quote: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(Modifier.height(12.dp))
        Text("THE STORY", fontSize = 11.sp, color = TextSecondary,
            letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(quote, fontSize = 15.sp, color = TextPrimary,
            fontStyle = FontStyle.Italic, textAlign = TextAlign.Center, lineHeight = 22.sp)
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
    }
}

// ─────────────────────────────────────────────────────────
// VITALITY STATS
// ─────────────────────────────────────────────────────────
@Composable
private fun VitalityStatsSection(stats: VitalityStats) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(YellowCard, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (stats.title.isNotBlank()) {
            Text(stats.title.uppercase(), fontSize = 12.sp, color = TextSecondary,
                fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(12.dp))
        }

        ConcentricRings(percentLabel = stats.percentLabel)

        Spacer(Modifier.height(12.dp))

        val statItems = listOfNotNull(
            stats.activity, stats.nutrition, stats.rest,
            stats.social, stats.energyLevel, stats.socialNeeds
        )
        val dotColors = listOf(GreenDark, Color(0xFF8B4513), Color(0xFFCD853F))

        statItems.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(10.dp).background(
                    dotColors.getOrElse(index) { GreenDark }, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(item.label, fontSize = 13.sp, color = TextSecondary, modifier = Modifier.weight(1f))
                if (item.value.isNotBlank()) {
                    Text(item.value, fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ConcentricRings(percentLabel: String?) {
    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(GreenDark))
        Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFF8B4513)))
        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFFD2691E)))
        Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(0xFFF5DEB3)))
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ) { Text("🐾", fontSize = 16.sp) }

        if (percentLabel != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val parts = percentLabel.split(" ")
                Text(parts[0], fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                if (parts.size > 1) {
                    Text(parts.drop(1).joinToString(" "), fontSize = 7.sp, color = Color.White.copy(0.8f))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// BLOCK C
// ─────────────────────────────────────────────────────────
@Composable
private fun BlockCSection(block: PetBlockC) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when (block.type) {
            BlockCType.DAILY_VIBE   -> DailyVibeBlock(block)
            BlockCType.CORE_TRAITS  -> CoreTraitsBlock(block)
            BlockCType.INFO_CARDS   -> InfoCardsBlock(block)
            BlockCType.AWARDS       -> AwardsBlock(block)
            BlockCType.HEALTH_LOG   -> HealthLogBlock(block)
            BlockCType.NEXT_RUN     -> NextRunBlock(block)
        }
    }
}

@Composable
private fun DailyVibeBlock(block: PetBlockC) {
    if (block.title.isNotBlank()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(4.dp).height(24.dp)
                .background(GreenDark, RoundedCornerShape(2.dp)))
            Spacer(Modifier.width(8.dp))
            Text(block.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
    block.text?.let { BlockCard(text = it) }
    block.items.forEach { item ->
        BlockCard(
            title   = item.title,
            text    = item.description,
            bgColor = if (item.highlighted) GreenPaleBg else YellowCard
        )
    }
}

@Composable
private fun CoreTraitsBlock(block: PetBlockC) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenPaleBg, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        if (block.title.isNotBlank()) {
            Text(block.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(Modifier.height(10.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            block.items.take(2).forEach { item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White.copy(0.6f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Text("🚀", fontSize = 18.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.description, fontSize = 11.sp, color = TextSecondary, lineHeight = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun InfoCardsBlock(block: PetBlockC) {
    if (block.items.size >= 2) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            block.items.take(2).forEach { item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(YellowCard, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Text("👥", fontSize = 20.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.description, fontSize = 11.sp, color = TextSecondary, lineHeight = 14.sp)
                }
            }
        }
        block.items.getOrNull(2)?.let { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenPaleBg, RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("👥", fontSize = 24.sp)
                Column {
                    Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.description, fontSize = 12.sp, color = TextSecondary)
                }
            }
        }
    } else {
        block.items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F0E8), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(40.dp).background(YellowCard, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("🐾", fontSize = 16.sp) }
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.description, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
                }
            }
        }
        block.text?.let { text ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(YellowCard, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                if (block.title.isNotBlank()) {
                    Text("♥", fontSize = 20.sp, color = TextPrimary)
                    Text(block.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Text(text, fontSize = 12.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun AwardsBlock(block: PetBlockC) {
    block.items.forEach { item ->
        val isRunner = item.title == "Smallest Runner"
        if (isRunner) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenPaleBg, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(GreenDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("🏃", fontSize = 20.sp) }
                Column {
                    Text(item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                    Text(item.description, fontSize = 12.sp, color = TextSecondary)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(YellowCard, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("❤", fontSize = 22.sp, color = Color(0xFF8B0000))
                    Text("🏆", fontSize = 18.sp)
                }
                Text(item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(item.description, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
private fun HealthLogBlock(block: PetBlockC) {
    if (block.title.isNotBlank()) {
        Text(block.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp))
    }
    block.items.forEach { item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(YellowCard, RoundedCornerShape(14.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier.size(36.dp).background(Color.White, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("📋", fontSize = 14.sp) }
                Column {
                    Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.description, fontSize = 11.sp, color = TextSecondary)
                }
            }
            Text("›", fontSize = 18.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun NextRunBlock(block: PetBlockC) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenDark, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        if (block.title.isNotBlank()) {
            Text(block.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        block.text?.let { Text(it, fontSize = 13.sp, color = Color.White.copy(0.85f)) }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(20.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = GreenLight)
        ) {
            Text("▶  Start Activity", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun BlockCard(title: String? = null, text: String, bgColor: Color = YellowCard) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        title?.let {
            Text(it, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(Modifier.height(4.dp))
        }
        Text(text, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
    }
}

// ─────────────────────────────────────────────────────────
// CAREGIVER NOTE
// ─────────────────────────────────────────────────────────
@Composable
private fun CaregiverSection(note: CaregiverNote) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Caregiver's Notes", fontSize = 18.sp, fontWeight = FontWeight.Bold,
            color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))

        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            note.caregiverName?.let { name ->
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFFFB347), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = name.first().toString(),
                        color      = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                note.caregiverName?.let { name ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        note.timeAgo?.let { time ->
                            Text(time, fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
                Text(note.text, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// Badge colors helper
// ─────────────────────────────────────────────────────────
private fun badgeColors(badge: PetBadge): Pair<Color, Color> = when (badge) {
    PetBadge.STAR_OF_WEEK       -> Color(0xFFCC7722)    to Color.White
    PetBadge.NEW_ARRIVAL        -> Color(0xFFCC7722)    to Color.White
    PetBadge.VET_CHECKED        -> Color(0xFF4A7C1F)    to Color.White
    PetBadge.ACTIVE_CAT,
    PetBadge.ACTIVE_NOW         -> Color(0xFF2D5016)    to Color.White
    PetBadge.MARATHON_SPIRIT    -> Color(0xFFFDD5B4)    to Color(0xFF7A3000)
    PetBadge.HYPER_ACTIVE       -> Color(0xFFFDD5B4)    to Color(0xFF7A3000)
    PetBadge.ONE_YEAR_OLD       -> Color(0xFFFDD5B4)    to Color(0xFF7A3000)
    PetBadge.TOP_CLIMBER        -> Color(0xFF2D5016)    to Color.White
    PetBadge.CLIMBER_SPECIALIST -> Color(0xFF7CB342)    to Color.White
    else                        -> Color(0xFFC8E6C9)    to Color(0xFF2D5016)
}