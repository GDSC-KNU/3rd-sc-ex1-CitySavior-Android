package com.citysavior.android.presentation.main.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.main.home.HomeScreen



fun NavGraphBuilder.homeGraph(
    //TODO xxClick: () -> Unit,
){
    composable(route = HomeRoute.HOME.route) {
        HomeScreen()
    }
}

sealed class HomeRoute(val korean: String, val route: String) {
    object HOME: HomeRoute("홈", "home")
    companion object{
        val START_ROUTE = HOME.route
    }
}