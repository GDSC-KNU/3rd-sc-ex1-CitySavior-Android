package com.citysavior.android.navigation

import com.citysavior.android.presentation.auth.navigation.AuthRoute
import com.citysavior.android.presentation.common.navigation.MAIN_GRAPH_ROUTE_PATTERN

enum class TopLevelDestination(
    val routePath: String,
) {
    AUTH(
        routePath = AuthRoute.ONBOARDING.route,
    ),
    MAIN(
        routePath = MAIN_GRAPH_ROUTE_PATTERN,
    ),
}