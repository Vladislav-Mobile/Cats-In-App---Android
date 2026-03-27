package com.example.catsinapp.ui.components


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeHeader() {
    Text("Home Header")
}

@Composable
fun SectionTitle(title: String) {
    Text(title)
}

@Composable
fun MiniPetCard(pet: Any, onClick: () -> Unit = {}) {
    Text("MiniPetCard")
}

@Composable
fun GridPetCard(pet: Any, onClick: () -> Unit = {}) {
    Text("GridPetCard")
}