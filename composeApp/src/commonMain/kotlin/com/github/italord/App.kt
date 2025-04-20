package com.github.italord

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.italord.screens.CalendarScreen
import com.github.italord.screens.DetailsScreen
import com.github.italord.screens.TimeScreen

@Composable
fun App() {

    val navController = rememberNavController()
    val mainViewmodel = remember { MainViewModel() }

    LaunchedEffect(Unit) { mainViewmodel.getDatesFromMonthYear("Apr2025") }

    val state = mainViewmodel.screenState.collectAsState().value

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = "Calendar"
    ) {
        composable(route = "Calendar") {
            CalendarScreen(navController, mainViewmodel, state)
        }
        composable(route = "Time") {
            TimeScreen(navController)
        }
        composable(route = "Details") {
            DetailsScreen(navController)
        }
    }

}