package com.citysavior.android.domain.repository.auth

import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.model.common.Async

interface AuthRepository {
    suspend fun login(): Async<JwtToken>
    suspend fun signUp(): Async<JwtToken>
    suspend fun getAfterOnBoarding(): Async<Boolean>
    suspend fun setAfterOnBoarding(): Async<Unit>
}
