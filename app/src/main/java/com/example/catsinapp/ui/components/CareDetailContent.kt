package com.example.catsinapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.catsinapp.data.model.CareCategory

@Composable
fun CareDetailContent(category: CareCategory) {
    Text(text = "Detail: ${category.title}")
}