package com.citysavior.android.presentation.main.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.main.home.HomeScreen

const val HOME_GRAPH_ROUTE_PATTERN = "/home"

fun NavGraphBuilder.homeGraph(
    //TODO xxClick: () -> Unit,
){
    navigation(
        route = HOME_GRAPH_ROUTE_PATTERN,
        startDestination = HomeRoute.HOME.route,
    ) {
        composable(route = HomeRoute.HOME.route) {
            HomeScreen()
        }

    }
}

enum class HomeRoute(val korean: String, val route: String) {
    HOME("홈", "home"),
}