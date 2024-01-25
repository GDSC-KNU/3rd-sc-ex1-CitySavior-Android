package com.citysavior.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.citysavior.android.presentation.common.navigation.mainGraph
import com.citysavior.android.ui.CitySaviorAppState
import timber.log.Timber

@Composable
fun CitySaviorNavHost(
    appState: CitySaviorAppState,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.MAIN.routePath,
) {
    Timber.d("StartDestination: $startDestination")
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ){
        //TODO
//        loginGraph(navController)
        mainGraph(navController)
    }
}