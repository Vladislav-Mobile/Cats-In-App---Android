package com.example.catsinapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.catsinapp.R
import com.example.catsinapp.debug.AppLogger
import com.example.catsinapp.ui.care.CareScreen
import com.example.catsinapp.ui.history.HistoryScreen
import com.example.catsinapp.ui.home.HomeScreen
import com.example.catsinapp.ui.petdetail.PetDetailScreen
import com.example.catsinapp.ui.profile.ProfileScreen
import com.example.catsinapp.ui.store.StoreScreen
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextSecondary
import com.example.catsinapp.ui.weight.WeightEntryScreen

private data class NavItem(val screen: Screen, val label: String, val iconRes: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navItems = listOf(
        NavItem(Screen.Home,    "Home",    R.drawable.ic_nav_home),
        NavItem(Screen.Care,    "Care",    R.drawable.ic_nav_care),
        NavItem(Screen.History, "History", R.drawable.ic_nav_history),
        NavItem(Screen.Profile, "Profile", R.drawable.ic_nav_profile),
        NavItem(Screen.Store,   "Store",   R.drawable.ic_nav_store),
    )

    val noBottomBarRoutes = listOf(Screen.PetDetail.route, Screen.WeightEntry.route)
    val backStack    by navController.currentBackStackEntryAsState()
    val currentRoute  = backStack?.destination?.route
    val showBottomBar = currentRoute !in noBottomBarRoutes

    LaunchedEffect(currentRoute) {
        currentRoute?.let { AppLogger.screenOpened(it) }
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White,
                    tonalElevation = androidx.compose.ui.unit.Dp(0f)) {
                    navItems.forEach { item ->
                        val selected = backStack?.destination
                            ?.hierarchy?.any { it.route == item.screen.route } == true
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
                            icon   = { Icon(painterResource(item.iconRes), item.label) },
                            label  = { Text(item.label) },
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
    ) { padding ->
        NavHost(navController = navController, startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)) {

            composable(Screen.Home.route) {
                val startTime = remember { System.currentTimeMillis() }
                LaunchedEffect(Unit) {
                    AppLogger.screenLoadTime("HomeScreen", System.currentTimeMillis() - startTime)
                }
                HomeScreen(onPetClick = { petId ->
                    navController.navigate(Screen.PetDetail.createRoute(petId))
                })
            }

            composable(Screen.Care.route) {
                val startTime = remember { System.currentTimeMillis() }
                LaunchedEffect(Unit) {
                    AppLogger.screenLoadTime("CareScreen", System.currentTimeMillis() - startTime)
                }
                CareScreen()
            }

            composable(Screen.History.route) {
                val startTime = remember { System.currentTimeMillis() }
                LaunchedEffect(Unit) {
                    AppLogger.screenLoadTime("HistoryScreen", System.currentTimeMillis() - startTime)
                }
                HistoryScreen()
            }

            composable(Screen.Profile.route) {
                val startTime = remember { System.currentTimeMillis() }
                LaunchedEffect(Unit) {
                    AppLogger.screenLoadTime("ProfileScreen", System.currentTimeMillis() - startTime)
                }
                ProfileScreen(navController = navController)
            }

            composable(Screen.Store.route) {
                val startTime = remember { System.currentTimeMillis() }
                LaunchedEffect(Unit) {
                    AppLogger.screenLoadTime("StoreScreen", System.currentTimeMillis() - startTime)
                }
                StoreScreen()
            }

            composable(Screen.PetDetail.route) { back ->
                val petId     = back.arguments?.getString("petId") ?: return@composable
                val startTime = remember { System.currentTimeMillis() }
                LaunchedEffect(Unit) {
                    AppLogger.screenOpened("PetDetailScreen", "petId=$petId")
                    AppLogger.screenLoadTime("PetDetailScreen", System.currentTimeMillis() - startTime)
                }
                PetDetailScreen(petId = petId, onBack = { navController.popBackStack() })
            }

            composable(Screen.WeightEntry.route) {
                val startTime = remember { System.currentTimeMillis() }
                LaunchedEffect(Unit) {
                    AppLogger.screenLoadTime("WeightEntryScreen", System.currentTimeMillis() - startTime)
                }
                WeightEntryScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}