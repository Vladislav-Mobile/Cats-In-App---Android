package com.example.catsinapp.ui.navigation

sealed class Screen(val route: String) {
    object Home        : Screen("home")
    object Care        : Screen("care")
    object History     : Screen("history")
    object Profile     : Screen("profile")
    object Store       : Screen("store")
    object PetDetail   : Screen("pet/{petId}") {
        fun route(petId: String) = "pet/$petId"
    }
    object WeightEntry : Screen("weight_entry")
}