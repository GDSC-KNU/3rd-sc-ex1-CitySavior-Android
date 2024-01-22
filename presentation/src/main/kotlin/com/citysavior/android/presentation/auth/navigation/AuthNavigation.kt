package com.citysavior.android.presentation.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val MAIN_GRAPH_ROUTE_PATTERN = "/auth"
const val AUTH_ROUTE = "auth"

fun NavGraphBuilder.authGraph(
   //TODO xxClick: () -> Unit,
){
    navigation(
        route = MAIN_GRAPH_ROUTE_PATTERN,
        startDestination = AUTH_ROUTE,
    ) {
        composable(route = AUTH_ROUTE) {

        }

    }
}