package com.citysavior.android.domain.repository.auth

import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.model.common.Async
import kotlinx.coroutines.flow.Flow

interface JwtTokenRepository {
    suspend fun saveJwtToken(jwtToken: JwtToken): Async<Unit>
    suspend fun saveAccessToken(accessToken: String): Async<Unit>
    suspend fun saveRefreshToken(refreshToken: String): Async<Unit>
    fun getJwtToken(): Flow<Async<JwtToken>>
    suspend fun deleteJwtToken(): Async<Unit>
}
