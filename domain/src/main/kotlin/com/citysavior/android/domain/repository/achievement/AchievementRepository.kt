package com.citysavior.android.domain.repository.achievement

import com.citysavior.android.domain.model.achievement.Achievement
import com.citysavior.android.domain.model.common.Async

interface AchievementRepository {
    suspend fun getAchievement(): Async<List<Achievement>>
}