package com.example.catsinapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.catsinapp.data.model.Pet
import com.example.catsinapp.data.model.PetBadge
import com.example.catsinapp.ui.theme.*

// ─────────────────────────────────────────────────────────
// HOME HEADER
// ─────────────────────────────────────────────────────────
@Composable
fun HomeHeader(onYouTubeClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier
                    .background(Color(0xFF1C1C1C), RoundedCornerShape(50.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text       = "Profile views",
                    color      = Color.White,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Box(
                    modifier = Modifier
                        .background(GreenDark, RoundedCornerShape(50.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text       = "120,826",
                        color      = Color.White,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text       = "Mobile app from Vlad Kazachek",
                fontSize   = 15.sp,
                fontWeight = FontWeight.Bold,
                color      = GreenDark
            )
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFF0000))
                .clickable { onYouTubeClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "▶", color = Color.White, fontSize = 14.sp)
        }
    }
}

// ─────────────────────────────────────────────────────────
// SECTION TITLE
// ─────────────────────────────────────────────────────────
@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp, bottom = if (subtitle != null) 2.dp else 8.dp)
    ) {
        Text(
            text       = title,
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary
        )
        if (subtitle != null) {
            Text(text = subtitle, fontSize = 13.sp, color = TextSecondary)
        }
    }
}

// ─────────────────────────────────────────────────────────
// BADGE CHIP
// ─────────────────────────────────────────────────────────
@Composable
fun PetBadgeChip(badge: PetBadge) {
    val (bg, fg) = when (badge) {
        PetBadge.STAR_OF_WEEK       -> Color(0xFF1C1C1C) to Color.White
        PetBadge.NEW_ARRIVAL        -> Color(0xFFCC7722) to Color.White
        PetBadge.VET_CHECKED        -> Color(0xFF4A7C1F) to Color.White
        PetBadge.ACTIVE_CAT,
        PetBadge.ACTIVE_NOW         -> GreenDark         to Color.White
        PetBadge.MARATHON_SPIRIT    -> Color(0xFFFDD5B4) to Color(0xFF7A3000)
        PetBadge.HYPER_ACTIVE       -> Color(0xFFFDD5B4) to Color(0xFF7A3000)
        PetBadge.TOP_CLIMBER        -> GreenDark         to Color.White
        PetBadge.CLIMBER_SPECIALIST -> GreenLight        to Color.White
        else                        -> GreenPale         to GreenDark
    }
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(50.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text          = badge.label.uppercase(),
            color         = fg,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

// ─────────────────────────────────────────────────────────
// STAR PET CARD — большая карточка Stars of the Week
// ─────────────────────────────────────────────────────────
@Composable
fun StarPetCard(pet: Pet, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model              = pet.imageRes,
            contentDescription = pet.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxWidth()
                .height(220.dp),
            placeholder        = painterResource(pet.imageRes),
            error              = painterResource(pet.imageRes)
        )

        // Тёмный градиент снизу
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f))
                    )
                )
        )

        // Бейджи вверху слева
        Row(
            modifier              = Modifier.align(Alignment.TopStart).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            pet.badges.take(2).forEach { PetBadgeChip(it) }
        }

        // Имя + описание внизу
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text       = pet.name,
                color      = Color.White,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text     = pet.subtitle,
                color    = Color.White.copy(0.85f),
                fontSize = 13.sp,
                maxLines = 2
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// MINI PET CARD — New Arrivals горизонтальный скролл
// ─────────────────────────────────────────────────────────
@Composable
fun MiniPetCard(pet: Pet, onClick: () -> Unit) {
    val statusLabel = when {
        pet.secondChip?.value != null -> pet.secondChip.value.uppercase()
        pet.tags.any { it.contains("kitten", true) } -> "KITTEN"
        else -> "ADULT"
    }

    Column(
        modifier = Modifier
            .width(110.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model              = pet.imageRes,
            contentDescription = pet.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder        = painterResource(pet.imageRes),
            error              = painterResource(pet.imageRes)
        )
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Text(
                text       = pet.name,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
            Text(
                text       = statusLabel,
                fontSize   = 11.sp,
                color      = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// GRID PET CARD — Active Cats, стандартная 2-колонная
// ─────────────────────────────────────────────────────────
@Composable
fun GridPetCard(pet: Pet, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(GreenPaleBg)
            .clickable { onClick() }
            .padding(bottom = 10.dp)
    ) {
        AsyncImage(
            model              = pet.imageRes,
            contentDescription = pet.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
            placeholder        = painterResource(pet.imageRes),
            error              = painterResource(pet.imageRes)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text       = pet.name,
            fontSize   = 15.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary,
            modifier   = Modifier.padding(horizontal = 10.dp)
        )
        if (pet.subtitle.isNotBlank()) {
            Text(
                text       = pet.subtitle.take(40),
                fontSize   = 11.sp,
                color      = TextSecondary,
                modifier   = Modifier.padding(horizontal = 10.dp),
                maxLines   = 2,
                lineHeight = 14.sp
            )
        }
        // Специальный тег снизу
        val tag = pet.badges.firstOrNull {
            it == PetBadge.MARATHON_SPIRIT || it == PetBadge.HYPER_ACTIVE ||
                    it == PetBadge.CLIMBER_SPECIALIST || it == PetBadge.TOP_CLIMBER
        }
        if (tag != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text       = tag.label.uppercase(),
                fontSize   = 10.sp,
                color      = GreenDark,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// FEATURED PET CARD — горизонтальная карточка (Mochi/Bean)
// ─────────────────────────────────────────────────────────
@Composable
fun FeaturedPetCard(pet: Pet, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFFAEDE8))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model              = pet.imageRes,
            contentDescription = pet.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(14.dp)),
            placeholder        = painterResource(pet.imageRes),
            error              = painterResource(pet.imageRes)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = pet.name,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
            Text(
                text       = pet.subtitle,
                fontSize   = 12.sp,
                color      = TextSecondary,
                maxLines   = 2,
                lineHeight = 16.sp
            )
            val specialBadge = pet.badges.firstOrNull {
                it == PetBadge.CLIMBER_SPECIALIST || it == PetBadge.TOP_CLIMBER ||
                        it == PetBadge.MARATHON_SPIRIT
            }
            if (specialBadge != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text       = specialBadge.label.uppercase(),
                    fontSize   = 10.sp,
                    color      = GreenDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}