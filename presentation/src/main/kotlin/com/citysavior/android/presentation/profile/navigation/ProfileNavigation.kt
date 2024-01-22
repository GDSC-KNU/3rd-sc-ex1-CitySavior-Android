package com.citysavior.android.presentation.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.profile.base.ProfileScreen

const val PROFILE_GRAPH_ROUTE_PATTERN = "/profile"
const val PROFILE_ROUTE = "profile"

fun NavGraphBuilder.profileGraph(
    //TODO xxClick: () -> Unit,
){
    navigation(
        route = PROFILE_GRAPH_ROUTE_PATTERN,
        startDestination = PROFILE_ROUTE,
    ) {
        composable(route = PROFILE_ROUTE) {
            ProfileScreen()
        }

    }
}