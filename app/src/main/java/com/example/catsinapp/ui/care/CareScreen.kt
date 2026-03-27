package com.example.catsinapp.ui.care

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.data.model.CareCategory
import com.example.catsinapp.ui.components.CareCategoryItem
import com.example.catsinapp.ui.components.CareDetailContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareScreen() {

    var selectedCategory by remember {
        mutableStateOf<CareCategory?>(null)
    }
    val categories = DataSource.getCareCategories()

    Box(modifier = Modifier.fillMaxSize()) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(categories) { cat ->
                CareCategoryItem(category = cat) {
                    selectedCategory = cat
                }
            }
        }

        selectedCategory?.let { cat ->
            ModalBottomSheet(
                onDismissRequest = { selectedCategory = null }
            ) {
                CareDetailContent(cat)
            }
        }
    }
}