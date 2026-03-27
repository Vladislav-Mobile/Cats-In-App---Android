package com.example.catsinapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.*
import com.example.catsinapp.ui.care.CareScreen
import com.example.catsinapp.ui.history.HistoryScreen
import com.example.catsinapp.ui.home.HomeScreen
import com.example.catsinapp.ui.petdetail.PetDetailScreen
import com.example.catsinapp.ui.profile.ProfileScreen
import com.example.catsinapp.ui.weight.WeightEntryScreen



@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val bottomTabs = listOf(
        Screen.Home, Screen.Care,
        Screen.History, Screen.Profile
    )
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        bottomBar = {


            val showBottom = currentRoute in bottomTabs.map { it.route }
            if (showBottom) {
                NavigationBar(containerColor = Color.White) {
                    bottomTabs.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route)
                            },
                            icon = { },
                            label = { Text(text = screen.route) }
                        )
                    }
                }
            }
        }
    )
    { padding ->
        NavHost(navController, Screen.Home.route,
            Modifier.padding(padding)) {
            composable("weight") {
                WeightEntryScreen(navController)
            }
            composable(Screen.Home.route)    { HomeScreen(navController) }
            composable(Screen.Care.route)    { CareScreen() }
            composable(Screen.History.route) { HistoryScreen() }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable("pet/{petId}") { back ->
                val id = back.arguments?.getString("petId") ?: return@composable
                PetDetailScreen(id, navController)
            }
            composable(Screen.WeightEntry.route) {
                WeightEntryScreen(navController)
            }
        }
    }
}