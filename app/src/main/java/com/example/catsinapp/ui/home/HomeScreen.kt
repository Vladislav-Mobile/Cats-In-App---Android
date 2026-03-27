package com.example.catsinapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.ui.components.*
import com.example.catsinapp.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    val stars    = DataSource.getStarsOfTheWeek()
    val arrivals = DataSource.getNewArrivals()
    val active   = DataSource.getActiveCats()

    LazyColumn {
        item { HomeHeader() }
        item { SectionTitle("Stars of the Week") }
        items(stars) { pet ->
            StarPetCard(pet) {
                navController.navigate(
                    Screen.PetDetail.route(pet.id)
                )
            }
        }
        item { SectionTitle("New Arrivals") }
        item {
            LazyRow {
                items(arrivals) { pet ->
                    MiniPetCard(pet) {
                        navController.navigate(
                            Screen.PetDetail.route(pet.id)
                        )
                    }
                }
            }
        }
        item { SectionTitle("Active Cats") }
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.heightIn(max = 600.dp)
            ) {
                items(active) { pet ->
                    GridPetCard(pet) {
                        navController.navigate(
                            Screen.PetDetail.route(pet.id)
                        )
                    }
                }
            }
        }
    }
}