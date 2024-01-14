package com.citysavior.android.data.repository.achievement

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.domain.model.achievement.Achievement
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.repository.achievement.AchievementRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AchievementRepository {
    override suspend fun getAchievement(): Async<List<Achievement>> {
        TODO("Not yet implemented")
    }
}