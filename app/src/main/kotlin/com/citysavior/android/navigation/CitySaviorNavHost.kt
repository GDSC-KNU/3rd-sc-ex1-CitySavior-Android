package com.citysavior.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.citysavior.android.presentation.auth.navigation.authGraph
import com.citysavior.android.presentation.main.navigation.mainGraph
import com.citysavior.android.ui.CitySaviorAppState
import timber.log.Timber

@Composable
fun CitySaviorNavHost(
    appState: CitySaviorAppState,
    modifier: Modifier = Modifier,
    startDestination: TopLevelDestination = TopLevelDestination.START,
) {
    Timber.d("StartDestination: $startDestination")
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination.routePath,
        modifier = modifier,
    ){
        authGraph(
            onboardingButtonClick = {// 온보딩 이후 로그인
                navController.navigate(TopLevelDestination.AUTH.routePath){
                    popUpTo(TopLevelDestination.START.routePath){
                        inclusive = true
                    }
                }
            },
            loginButtonClick = {// 로그인 이후 메인
                navController.navigate(TopLevelDestination.MAIN.routePath){
                    popUpTo(TopLevelDestination.AUTH.routePath){
                        inclusive = true
                    }
                }
            },
            signupButtonClick = {// 회원가입 이후 메인
                navController.navigate(TopLevelDestination.MAIN.routePath){
                    popUpTo(TopLevelDestination.AUTH.routePath){
                        inclusive = true
                    }
                }
            },
        )
        mainGraph(navController)
    }
}