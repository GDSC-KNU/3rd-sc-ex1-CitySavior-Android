package com.citysavior.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citysavior.android.domain.model.common.onError
import com.citysavior.android.domain.model.common.onSuccess
import com.citysavior.android.domain.repository.auth.AuthRepository
import com.citysavior.android.domain.repository.auth.JwtTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val jwtTokenRepository: JwtTokenRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = flow {
        jwtTokenRepository.getJwtToken()
            .collect {
                Timber.d("collect: $it")
                it.onSuccess {
                    emit(MainActivityUiState.AfterLogin)
                }.onError {
                    Timber.d("collect Error!: $it")
                    authRepository.getAfterOnBoarding().onSuccess {
                        if(it){
                            emit(MainActivityUiState.BeforeLogin)
                        }else{
                            emit(MainActivityUiState.Initial)
                        }
                    }.onError {
                        emit(MainActivityUiState.Initial)
                    }
                }
            }
    }.stateIn(viewModelScope, SharingStarted.Lazily, MainActivityUiState.Loading)
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    object Initial : MainActivityUiState
    object BeforeLogin : MainActivityUiState
    object AfterLogin : MainActivityUiState
}