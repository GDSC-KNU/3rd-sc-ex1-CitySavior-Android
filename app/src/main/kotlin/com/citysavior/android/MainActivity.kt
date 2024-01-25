package com.citysavior.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.citysavior.android.core.utils.NetworkMonitor
import com.citysavior.android.navigation.TopLevelDestination
import com.citysavior.android.ui.CitySaviorApp
import com.citysavior.android.ui.theme.CitySaviorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var networkMonitor: NetworkMonitor
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect{}
            }
        }

        splashScreen.setKeepVisibleCondition {
            uiState == MainActivityUiState.Loading
        }

        setContent {
            CitySaviorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination = when (uiState) {
                        MainActivityUiState.Loading -> TopLevelDestination.START
                        MainActivityUiState.Initial -> TopLevelDestination.START
                        MainActivityUiState.BeforeLogin -> TopLevelDestination.AUTH
                        MainActivityUiState.AfterLogin -> TopLevelDestination.MAIN
                    }
                    when(uiState){
                        MainActivityUiState.Loading -> Spacer(modifier =Modifier.fillMaxSize())
                        else ->{
                            CitySaviorApp(
                                networkMonitor = networkMonitor,
                                startDestination = startDestination,
                            )
                        }
                    }
                }
            }
        }
    }
}
