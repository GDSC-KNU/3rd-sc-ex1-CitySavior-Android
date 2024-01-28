package com.citysavior.android.presentation.main.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.citysavior.android.presentation.R
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.utils.noRippleClickable
import com.citysavior.android.presentation.main.home.navigation.HomeRoute
import com.citysavior.android.presentation.main.home.navigation.homeGraph
import com.citysavior.android.presentation.main.map.navigation.MapRoute
import com.citysavior.android.presentation.main.map.navigation.mapGraph
import com.citysavior.android.presentation.main.profile.navigation.ProfileRoute
import com.citysavior.android.presentation.main.profile.navigation.profileGraph

enum class BottomNavRouter(
    val routePath: String,
    val korean: String,
    @DrawableRes val icon: Int,
) {
    HOME(HomeRoute.START_ROUTE,"홈", R.drawable.home_icon),
    MAP(MapRoute.START_ROUTE,"지도", R.drawable.map_icon),
    PROFILE(ProfileRoute.START_ROUTE,"내정보", R.drawable.profile_icon),
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRootTab(){
    val items = BottomNavRouter.values().toList()

    val mainNavHostController = rememberNavController()
    Scaffold(
        bottomBar = {
            val navBackStackEntry by mainNavHostController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute in items.map { it.routePath }) {
                NavigationBar(
                    modifier = Modifier
                        .height(55.dp)
                        .shadow(
                            elevation = 15.dp,
                            spotColor = Color(0xff000000),
                            ambientColor = Color(0xff000000),
                        ),
                    containerColor = Colors.WHITE,
                ) {
                    items.forEach { item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .noRippleClickable {
                                    mainNavHostController.navigate(item.routePath) {
                                        popUpTo(mainNavHostController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                        ) {
                            val select = item.routePath == currentRoute
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(23.dp),
                                tint = if (select) Color(0xFF2E2E2E) else Color(0xFFD5D5D5),
                            )
                            Text(text = item.korean, style = TextStyles.CONTENT_SMALL2_STYLE)
                        }
                    }
                }

            }
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavHostController,
            startDestination = BottomNavRouter.HOME.routePath,
            modifier = Modifier.padding(innerPadding)
        ) {
            homeGraph()
            mapGraph()
            profileGraph()
        }
    }
}