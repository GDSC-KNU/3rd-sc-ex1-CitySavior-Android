package com.citysavior.android.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.citysavior.android.core.utils.NetworkMonitor
import com.citysavior.android.navigation.TopLevelDestination
import com.citysavior.android.navigation.TopLevelDestination.*
import com.citysavior.android.presentation.auth.navigation.AuthRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberCitySaviorAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): CitySaviorAppState {
    return remember(
        navController,
        coroutineScope,
        networkMonitor,
    ) {
        CitySaviorAppState(
            navController,
            coroutineScope,
            networkMonitor,
        )
    }
}

@Stable
class CitySaviorAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            AUTH.routePath -> AUTH
            MAIN.routePath -> MAIN
            else -> null
        }


    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    @OptIn(ExperimentalStdlibApi::class)
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().toList()



}