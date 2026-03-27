package com.example.catsinapp.ui.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Care : Screen("care")
    object History : Screen("history")
    object Profile : Screen("profile")
    object WeightEntry : Screen("weight")

    // ❗ пока уберём Store (у тебя нет экрана)
    // object Store : Screen("store")

    object PetDetail : Screen("pet_detail/{petId}") {
        fun route(petId: String) = "pet_detail/$petId"
    }
}