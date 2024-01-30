package com.citysavior.android.presentation.main.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.main.profile.ProfileScreen


fun NavGraphBuilder.profileGraph(
    //TODO xxClick: () -> Unit,
){
    composable(route = ProfileRoute.PROFILE.route) {
        ProfileScreen()
    }
}

sealed class ProfileRoute(val korean: String, val route: String) {
    object PROFILE : ProfileRoute("프로필", "profile")
    companion object{
        val START_ROUTE = PROFILE.route
    }
}