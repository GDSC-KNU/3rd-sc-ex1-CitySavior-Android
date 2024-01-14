package com.citysavior.android.data.repository.auth

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.repository.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun login(): Async<JwtToken> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(): Async<JwtToken> {
        TODO("Not yet implemented")
    }
}