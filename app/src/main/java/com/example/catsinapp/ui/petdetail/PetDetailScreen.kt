package com.example.catsinapp.ui.petdetail


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.ui.components.BlockA
import com.example.catsinapp.ui.components.BlockB
import com.example.catsinapp.ui.components.BlockC
import com.example.catsinapp.ui.components.BlockD
import com.example.catsinapp.ui.components.CtaButton
import com.example.catsinapp.ui.components.PetHeroSection
import com.example.catsinapp.ui.components.PetInfoChips

@Composable
fun PetDetailScreen(
    petId: String,
    onBack: () -> Unit
) {
    val pet = DataSource.getPetById(petId) ?: return

    LazyColumn {
        item { PetHeroSection(pet) }
        item { PetInfoChips(pet) }

        if (pet.personalityText != null) {
            item { BlockA(pet.personalityText) }
        }

        if (pet.vitalityStats != null) {
            item { BlockB(pet.vitalityStats) }
        }

        if (pet.blockC != null) {
            item { BlockC(pet.blockC) }
        }

        if (pet.caregiverNote != null) {
            item { BlockD(pet.caregiverNote) }
        }

        if (pet.ctaText.isNotBlank()) {
            item { CtaButton(pet.ctaText) }
        }
    }
}