package com.citysavior.android.data.repository.user

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.data.dto.user.response.toDomain
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserInfo
import com.citysavior.android.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : UserRepository {
    override suspend fun getUserInfo(): Async<UserInfo> {
        return invokeApiAndConvertAsync(
            api = { apiService.getUserInfo() },
            convert = { it.toDomain() }
        )
    }
}