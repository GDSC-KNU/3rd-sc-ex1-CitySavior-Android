package com.citysavior.android.domain.repository.auth

import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserRole

interface AuthRepository {
    suspend fun login(): Async<JwtToken>
    suspend fun signUp(): Async<JwtToken>
    suspend fun getAfterOnBoarding(): Async<Boolean>
    suspend fun setAfterOnBoarding(): Async<Unit>
    suspend fun getUserRole(): Async<UserRole>
    suspend fun changeUserRole(): Async<Unit>

    suspend fun isExistUuid(): Boolean // 회원가입 여부 확인
}
