package com.citysavior.android.navigation

import com.citysavior.android.presentation.auth.navigation.AuthRoute
import com.citysavior.android.presentation.main.navigation.MainRoute

enum class TopLevelDestination(
    val routePath: String,
) {
    START(
        routePath = AuthRoute.ONBOARDING.route,
    ),
    AUTH(
        routePath = AuthRoute.SIGNUP.route,
    ),
    MAIN(
        routePath = MainRoute.MAIN.route,
    ),
}