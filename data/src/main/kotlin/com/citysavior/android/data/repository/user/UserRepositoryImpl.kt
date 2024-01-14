package com.citysavior.android.data.repository.user

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.data.dto.user.response.toDomain
import com.citysavior.android.data.entity.category.CategoryDatabase
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserInfo
import com.citysavior.android.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val db: CategoryDatabase,
) : UserRepository {
    override suspend fun getUserInfo(): Async<UserInfo> {
        return invokeApiAndConvertAsync(
            api = { apiService.getUserInfo() },
            convert = {
                UserInfo(
                    totalReportCount = it.totalReportCount,
                    totalRepairedCount = it.totalRepairedCount,
                    achieveCollectCount = it.achieveCollectCount,
                    achieveProgressingList = it.achieveProgressing.map {dto ->
                        dto.toDomain(db.categoryDao().getCategoryById(dto.categoryId))
                    },
                )
            }
        )
    }
}