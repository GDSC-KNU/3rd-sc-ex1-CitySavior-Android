package com.citysavior.android.data.repository.achievement

import com.citysavior.android.data.api.ApiClient
import com.citysavior.android.data.dto.achievement.response.toDomain
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.achievement.Achievement
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.repository.achievement.AchievementRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
) : AchievementRepository {
    override suspend fun getAchievement(): Async<List<Achievement>> {
        return invokeApiAndConvertAsync(
            api = { apiClient.getAchievementInfo() },
            convert = { it.achievements.toDomain() }
        )
    }
}