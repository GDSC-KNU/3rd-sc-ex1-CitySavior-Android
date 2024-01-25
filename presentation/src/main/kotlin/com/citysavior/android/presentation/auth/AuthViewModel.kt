package com.citysavior.android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citysavior.android.domain.model.common.onError
import com.citysavior.android.domain.model.common.onSuccess
import com.citysavior.android.domain.repository.auth.AuthRepository
import com.citysavior.android.domain.repository.auth.JwtTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val jwtTokenRepository: JwtTokenRepository,
): ViewModel() {

    suspend fun signUp() : String?{
        val token = authRepository.signUp()
        token.onSuccess {
            jwtTokenRepository.saveJwtToken(it)
            return null
        }.onError {
            return it.message
        }
        assert(false)//unreachable
        return null
    }

    suspend fun onBoardingDone(){
        authRepository.setAfterOnBoarding()
    }
}