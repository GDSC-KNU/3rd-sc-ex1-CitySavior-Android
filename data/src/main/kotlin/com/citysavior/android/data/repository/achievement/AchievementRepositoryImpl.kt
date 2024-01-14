package com.citysavior.android.data.repository.achievement

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.data.dto.achievement.response.toDomain
import com.citysavior.android.data.entity.category.CategoryDatabase
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.achievement.Achievement
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.repository.achievement.AchievementRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val db: CategoryDatabase,
) : AchievementRepository {
    override suspend fun getAchievement(): Async<List<Achievement>> {
        return invokeApiAndConvertAsync(
            api = { apiService.getAchievementInfo() },
            convert = {
                this.achievements.map { achievementDto ->
                    achievementDto.toDomain(
                       db.categoryDao().getCategoryById(achievementDto.categoryId)
                    )
                }
            }
        )
    }
}