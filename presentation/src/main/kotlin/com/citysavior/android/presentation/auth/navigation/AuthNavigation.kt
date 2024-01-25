package com.citysavior.android.presentation.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.auth.onboarding.OnboardingScreen


fun NavGraphBuilder.authGraph(
   //TODO xxClick: () -> Unit,
){
    composable(route = AuthRoute.LOGIN.route) {
        //TODO LoginScreen()
    }
    composable(route = AuthRoute.ONBOARDING.route) {
        OnboardingScreen(
            startButtonClick = {

            }
        )
    }
}

enum class AuthRoute(val korean: String, val route: String) {
    LOGIN("로그인", "login"),
    ONBOARDING("온보딩", "onboarding"),
}