package com.citysavior.android.presentation.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.citysavior.android.presentation.auth.AuthViewModel
import com.citysavior.android.presentation.common.layout.DefaultLayout

@Composable
fun LoginScreen(
    loginButtonClick: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
){
    LaunchedEffect(Unit) {
        authViewModel.login()
    }
    DefaultLayout {

    }
}