package com.github.italord

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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

    LaunchedEffect(Unit) { mainViewmodel.getDatesFromMonthYear() }

    val screenState = mainViewmodel.screenState.collectAsState().value
    val submittedValue = remember { mutableStateOf<String?>(null) }

    if (submittedValue.value != null) {
        AlertDialog(
            onDismissRequest = { submittedValue.value = null },
            confirmButton = {
                TextButton(onClick = { submittedValue.value = null }) {
                    Text("OK")
                }
            },
            title = { Text("Payload") },
            text = { Text("${submittedValue.value}") }
        )
    }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = "Calendar"
    ) {
        composable(route = "Calendar") {
            CalendarScreen(
                mainViewModel = mainViewmodel,
                screenState = screenState,
                onDateSelected = {
                    mainViewmodel.selectDate(it)
                    navController.navigate("Time")
                })
        }
        composable(route = "Time") {
            TimeScreen(
                mainViewModel = mainViewmodel,
                screenState = screenState,
                onBack = { navController.popBackStack() },
                onTimeSelected = {
                    mainViewmodel.selectTime(it)
                    navController.navigate("Details")
                }
            )
        }
        composable(route = "Details") {
            DetailsScreen(
                state = screenState,
                onBack = { navController.popBackStack() },
                onSubmit = {
                    submittedValue.value = it
                    println(it)
                }
            )
        }
    }
}