package com.citysavior.android.presentation.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.citysavior.android.presentation.auth.login.LoginScreen
import com.citysavior.android.presentation.auth.login.SignupScreen
import com.citysavior.android.presentation.auth.onboarding.OnboardingScreen


fun NavGraphBuilder.authGraph(
    onboardingButtonClick: () -> Unit = {},
    loginButtonClick: () -> Unit = {},
    signupButtonClick : () -> Unit = {},
){
    composable(route = AuthRoute.LOGIN.route) {
        LoginScreen(
            loginButtonClick = loginButtonClick,
        )
    }
    composable(route = AuthRoute.SIGNUP.route) {
        SignupScreen(
            signupButtonClick = signupButtonClick,
        )
    }
    composable(route = AuthRoute.ONBOARDING.route) {
        OnboardingScreen(
            startButtonClick = onboardingButtonClick,
        )
    }
}

enum class AuthRoute(val korean: String, val route: String) {
    LOGIN("로그인", "login"),
    SIGNUP("회원가입", "signup"),
    ONBOARDING("온보딩", "onboarding"),
}