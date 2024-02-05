package com.citysavior.android.domain.repository.user

import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInfo(): Async<UserInfo>

    suspend fun saveUserPoint(point: Point): Async<Unit>
    fun getUserPoint(): Flow<Async<Point>>
}
