package com.example.catsinapp.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.data.model.Pet
import com.example.catsinapp.ui.components.*

@Composable
fun HomeScreen(onPetClick: (String) -> Unit) {
    val stars    = DataSource.getStarsOfTheWeek()
    val arrivals = DataSource.getNewArrivals()
    val active   = DataSource.getActiveCats()
    val context  = LocalContext.current

    LazyColumn(
        modifier       = Modifier.fillMaxSize().background(Color.White),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            HomeHeader(onYouTubeClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/@QAMobileinternational"))
                )
            })
        }

        item { SectionTitle("Stars of the Week") }
        items(stars, key = { it.id }) { pet ->
            StarPetCard(pet = pet, onClick = { onPetClick(pet.id) })
        }

        item {
            SectionTitle(
                title    = "New Arrivals",
                subtitle = "Fresh faces looking for a home"
            )
        }
        item {
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(arrivals, key = { it.id }) { pet ->
                    MiniPetCard(pet = pet, onClick = { onPetClick(pet.id) })
                }
            }
        }

        item {
            SectionTitle(
                title    = "Active Cats",
                subtitle = "Full of energy and life"
            )
        }
        item {
            ActiveCatsSection(pets = active, onPetClick = onPetClick)
        }
    }
}

@Composable
private fun ActiveCatsSection(pets: List<Pet>, onPetClick: (String) -> Unit) {
    val featuredIds = setOf("mochi", "bean")
    val groups      = mutableListOf<List<Pet>>()
    val buffer      = mutableListOf<Pet>()

    pets.forEach { pet ->
        if (pet.id in featuredIds) {
            if (buffer.isNotEmpty()) { groups.addAll(buffer.chunked(2)); buffer.clear() }
            groups.add(listOf(pet))
        } else {
            buffer.add(pet)
        }
    }
    if (buffer.isNotEmpty()) groups.addAll(buffer.chunked(2))

    Column(
        modifier            = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groups.forEach { group ->
            if (group.size == 1 && group[0].id in featuredIds) {
                FeaturedPetCard(pet = group[0], onClick = { onPetClick(group[0].id) })
            } else {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    group.forEach { pet ->
                        Box(Modifier.weight(1f)) {
                            GridPetCard(pet = pet, onClick = { onPetClick(pet.id) })
                        }
                    }
                    if (group.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}