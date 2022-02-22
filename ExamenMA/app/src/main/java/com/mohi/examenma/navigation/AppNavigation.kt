package com.mohi.examenma.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohi.examenma.components.AddEntityScreen
import com.mohi.examenma.components.DisplayAllScreen
import com.mohi.examenma.components.DisplayTopScreen
import com.mohi.examenma.model.viewmodel.EntitiesViewModel

@ExperimentalMaterialApi
@Composable
fun AppNavigation(
    viewModel: EntitiesViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Screen1.route) {
        composable(
            route = Screen.Screen1.route
        ) {
            DisplayAllScreen(
                onClick = {
                    navController.navigate(Screen.Screen2.route)
                },
                onSwitchToTopView = {
                    navController.navigate(Screen.Screen3.route)
                }
            )
        }
        composable(
            route = Screen.Screen2.route
        ) {
            AddEntityScreen(
                onClick =  { nume: String, medie: Int, etaj: Int, orientare: Boolean ->
                    viewModel.add(nume, medie, etaj, orientare)
                    navController.navigate(Screen.Screen1.route)
                }
            )
        }
        composable(
            route = Screen.Screen3.route
        ) {
            DisplayTopScreen()
        }
    }
}