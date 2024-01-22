package com.citysavior.android.presentation.main.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.main.profile.ProfileScreen

const val PROFILE_GRAPH_ROUTE_PATTERN = "/profile"

fun NavGraphBuilder.profileGraph(
    //TODO xxClick: () -> Unit,
){
    navigation(
        route = PROFILE_GRAPH_ROUTE_PATTERN,
        startDestination = ProfileRoute.PROFILE.route,
    ) {
        composable(route = ProfileRoute.PROFILE.route) {
            ProfileScreen()
        }

    }
}

enum class ProfileRoute(val korean: String, val route: String) {
    PROFILE("프로필", "profile"),
}