package com.citysavior.android.domain.repository.user

import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserInfo

interface UserRepository {
    suspend fun getUserInfo(): Async<UserInfo>
}
