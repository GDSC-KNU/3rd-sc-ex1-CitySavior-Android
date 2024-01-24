package com.citysavior.android.presentation.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation


fun NavGraphBuilder.mainGraph(
    navController: NavController,
){
    composable(route = MainRoute.MAIN.route) {
        MainRootTab()
    }
}

sealed class MainRoute(val korean: String, val route: String) {
    object MAIN: MainRoute("메인", "/")
}