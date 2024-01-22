package com.citysavior.android.navigation

import com.citysavior.android.presentation.auth.navigation.AuthRoute
import com.citysavior.android.presentation.main.navigation.MainRoute

enum class TopLevelDestination(
    val routePath: String,
) {
    AUTH(
        routePath = AuthRoute.ONBOARDING.route,
    ),
    MAIN(
        routePath = MainRoute.MAIN.route,
    ),
}