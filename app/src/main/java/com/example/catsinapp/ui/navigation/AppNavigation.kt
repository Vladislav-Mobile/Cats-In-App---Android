package com.example.catsinapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.catsinapp.R
import com.example.catsinapp.ui.care.CareScreen
import com.example.catsinapp.ui.history.HistoryScreen
import com.example.catsinapp.ui.home.HomeScreen
import com.example.catsinapp.ui.petdetail.PetDetailScreen
import com.example.catsinapp.ui.profile.ProfileScreen
import com.example.catsinapp.ui.theme.*
import com.example.catsinapp.ui.weight.WeightEntryScreen

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconRes: Int
)


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val bottomItems = listOf(
        BottomNavItem(Screen.Home,    "Home",    R.drawable.ic_nav_home),
        BottomNavItem(Screen.Care,    "Care",    R.drawable.ic_nav_care),
        BottomNavItem(Screen.History, "History", R.drawable.ic_nav_history),
        BottomNavItem(Screen.Profile, "Profile", R.drawable.ic_nav_profile),
        BottomNavItem(Screen.Store,   "Store",   R.drawable.ic_nav_store),
    )

    // Экраны где bottom bar скрыт
    val screensWithoutBottomBar = listOf(
        Screen.PetDetail.route,
        Screen.WeightEntry.route
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute !in screensWithoutBottomBar

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = androidx.compose.ui.unit.Dp(0f)
                ) {
                    bottomItems.forEach { item ->
                        val selected = navBackStackEntry
                            ?.destination
                            ?.hierarchy
                            ?.any { destination -> destination.route == item.screen.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick  = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(item.iconRes),
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = GreenDark,
                                selectedTextColor   = GreenDark,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor      = Color(0xFFD4EDAA)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Home.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onPetClick = { petId ->
                    navController.navigate(Screen.PetDetail.createRoute(petId))
                })
            }
            composable(Screen.Care.route) {
                CareScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onChangeData = {
                        navController.navigate(Screen.WeightEntry.route)
                    }
                )
            }
            composable(Screen.Store.route) {
                // Store placeholder
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .wrapContentSize()
                ) {
                    Text("Store — Coming soon")
                }
            }
            composable(Screen.PetDetail.route) { backStack ->

                val petId = backStack.arguments?.getString("petId") ?: return@composable

                PetDetailScreen(
                    petId = petId,
                    navController = navController
                )
            }
            composable(Screen.WeightEntry.route) {
                WeightEntryScreen(onBack = { navController.popBackStack() })
            }

        }
    }
}