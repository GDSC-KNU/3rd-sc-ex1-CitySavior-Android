package com.citysavior.android.presentation.main.map.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.main.map.MapScreen


fun NavGraphBuilder.mapGraph(
    //TODO xxClick: () -> Unit,
){
    composable(route = MapRoute.MAP.route) {
        MapScreen()
    }
}

sealed class MapRoute(val korean: String, val route: String) {
    object MAP: MapRoute("지도", "map")
    companion object{
        val START_ROUTE = MAP.route
    }
}