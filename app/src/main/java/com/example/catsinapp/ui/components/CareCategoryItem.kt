package com.example.catsinapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.catsinapp.data.model.CareCategory

@Composable
fun CareCategoryItem(
    category: CareCategory,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick
    ) {
        Text(text = category.title)
    }
}