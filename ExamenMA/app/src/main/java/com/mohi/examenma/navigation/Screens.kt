package com.mohi.examenma.navigation

sealed class Screen(val route: String) {
    object Screen1: Screen(route="screen1")
    object Screen2: Screen(route="screen2")
    object Screen3: Screen(route="screen3")
}