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
// BADGE — цветной ярлык поверх фото
// ─────────────────────────────────────────────────────────
@Composable
fun PetBadgeChip(badge: PetBadge) {
    val bgColor = GreenPale
    val textColor = GreenDark
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text       = badge.label,
            color      = textColor,
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─────────────────────────────────────────────────────────
// STAR PET CARD — большая карточка для Stars of the Week
// ─────────────────────────────────────────────────────────
@Composable
fun CalendarStrip() {
    Text("CalendarStrip")
}

@Composable
fun SummaryCards() {
    Text("SummaryCards")
}

@Composable
fun FeedingLogList(logs: List<Any>) {
    Text("FeedingLogList")
}

@Composable
fun LogNextMealButton() {
    Text("LogNextMealButton")
}

@Composable
fun StarPetCardLarge(pet: Pet, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {

        AsyncImage(
            model = pet.imageRes,
            contentDescription = pet.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        // затемнение
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.65f)
                        )
                    )
                )
        )

        // бейджи (если будут)
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // PetBadgeChip(...)
        }

        // ТЕКСТ — ВНЕ Row!
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = pet.name,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = pet.subtitle,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                maxLines = 2
            )
        }
    }
}


// ─────────────────────────────────────────────────────────
// MINI PET CARD — для New Arrivals (горизонтальный скролл)
// ─────────────────────────────────────────────────────────
@Composable
fun MiniPetCard(pet: Pet, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(110.dp)
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
                .height(100.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            placeholder        = painterResource(pet.imageRes),
            error              = painterResource(pet.imageRes)
        )
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text       = pet.name,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
            Text(
                text     = pet.secondChip?.value ?: pet.status.name,
                fontSize = 11.sp,
                color    = TextSecondary
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// GRID PET CARD — для Active Cats (2 колонки)
// ─────────────────────────────────────────────────────────
@Composable
fun GridPetCard(pet: Pet, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GreenPaleBg)
            .clickable { onClick() }
            .padding(bottom = 8.dp)
    ) {
        AsyncImage(
            model              = pet.imageRes,
            contentDescription = pet.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            placeholder        = painterResource(pet.imageRes),
            error              = painterResource(pet.imageRes)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text       = pet.name,
            fontSize   = 15.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary,
            modifier   = Modifier.padding(horizontal = 8.dp)
        )
        if (pet.subtitle.isNotBlank()) {
            Text(
                text     = pet.subtitle.take(36),
                fontSize = 11.sp,
                color    = TextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 2
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// SECTION HEADER
// ─────────────────────────────────────────────────────────
@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp, bottom = 4.dp)
    ) {
        Text(
            text       = title,
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary
        )
        if (subtitle != null) {
            Text(
                text     = subtitle,
                fontSize = 13.sp,
                color    = TextSecondary
            )
        }
    }
}