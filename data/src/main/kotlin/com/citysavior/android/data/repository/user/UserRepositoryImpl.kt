package com.citysavior.android.data.repository.user

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserInfo
import com.citysavior.android.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getUserInfo(): Async<UserInfo> {
        TODO("Not yet implemented")
    }
}