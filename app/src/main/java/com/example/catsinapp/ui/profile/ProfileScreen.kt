package com.example.catsinapp.ui.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button

@Composable
fun ProfileScreen(onChangeData: () -> Unit) {
    Button(onClick = onChangeData) {
        Text("Изменить данные")
    }
}