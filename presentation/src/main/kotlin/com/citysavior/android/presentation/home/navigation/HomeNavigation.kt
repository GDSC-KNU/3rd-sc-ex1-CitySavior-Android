package com.citysavior.android.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.home.base.HomeScreen

const val HOME_GRAPH_ROUTE_PATTERN = "/home"
const val HOME_ROUTE = "home"

fun NavGraphBuilder.homeGraph(
    //TODO xxClick: () -> Unit,
){
    navigation(
        route = HOME_GRAPH_ROUTE_PATTERN,
        startDestination = HOME_ROUTE,
    ) {
        composable(route = HOME_ROUTE) {
            HomeScreen()
        }

    }
}