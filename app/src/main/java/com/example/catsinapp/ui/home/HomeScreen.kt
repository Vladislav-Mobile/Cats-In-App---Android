package com.example.catsinapp.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catsinapp.R
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.ui.components.GridPetCard
import com.example.catsinapp.ui.components.MiniPetCard
import com.example.catsinapp.ui.components.SectionTitle
import com.example.catsinapp.ui.components.StarPetCard
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

@Composable
fun HomeScreen(onPetClick: (String) -> Unit) {
    val stars    = DataSource.getStarsOfTheWeek()
    val arrivals = DataSource.getNewArrivals()
    val active   = DataSource.getActiveCats()
    val context  = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ── Header ──────────────────────────────────────
        item {
            HomeHeader(onYouTubeClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/@QAMobileinternational")
                )
                context.startActivity(intent)
            })
        }

        // ── Stars of the Week ────────────────────────────
        item {
            SectionTitle("Stars of the Week")
        }
        items(stars) { pet ->
            StarPetCard(pet = pet, onClick = { onPetClick(pet.id) })
        }

        // ── New Arrivals ─────────────────────────────────
        item {
            SectionTitle(
                title    = "New Arrivals",
                subtitle = "Fresh faces looking for a home"
            )
        }
        item {
            LazyRow(
                contentPadding       = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(arrivals) { pet ->
                    MiniPetCard(pet = pet, onClick = { onPetClick(pet.id) })
                }
            }
        }

        // ── Active Cats ──────────────────────────────────
        item {
            SectionTitle(
                title    = "Active Cats",
                subtitle = "Full of energy and life"
            )
        }
        item {
            ActiveCatsGrid(
                pets    = active,
                onClick = { onPetClick(it) }
            )
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

// ─────────────────────────────────────────────────────────
// Header — "Mobile app from Vlad Kazachek" + YouTube
// ─────────────────────────────────────────────────────────
@Composable
private fun HomeHeader(onYouTubeClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Profile views badge
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1A1A1A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text     = "Profile views  ",
                            color    = Color.White,
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(GreenDark, RoundedCornerShape(6.dp))
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
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text       = "Mobile app from Vlad Kazachek",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = GreenDark
                )
            }

            // YouTube button
            IconButton(onClick = onYouTubeClick) {
                Icon(
                    painter            = painterResource(R.drawable.ic_youtube),
                    contentDescription = "YouTube",
                    tint               = Color(0xFFFF0000),
                    modifier           = Modifier.size(32.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// Active Cats Grid — 2 колонки без LazyVerticalGrid
// (чтобы избежать nested scroll конфликта)
// ─────────────────────────────────────────────────────────
@Composable
private fun ActiveCatsGrid(
    pets: List<com.example.catsinapp.data.model.Pet>,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        // Разбиваем список по парам
        val rows = pets.chunked(2)
        rows.forEach { rowPets ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowPets.forEach { pet ->
                    Box(modifier = Modifier.weight(1f)) {
                        GridPetCard(pet = pet, onClick = { onClick(pet.id) })
                    }
                }
                // Если нечётное количество — пустая ячейка
                if (rowPets.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}