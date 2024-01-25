package com.citysavior.android.presentation.auth.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.citysavior.android.presentation.auth.AuthViewModel
import com.citysavior.android.presentation.common.layout.DefaultLayout

@Composable
fun SignupScreen(
    signupButtonClick: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
) {
    Log.d("SignupScreen", "SignupScreen")
    LaunchedEffect(Unit) {
        val isError = viewModel.signUp()
        if(isError == null){
            signupButtonClick.invoke()
        }else{
            //TODO error handling
        }
    }
    DefaultLayout {

    }
}