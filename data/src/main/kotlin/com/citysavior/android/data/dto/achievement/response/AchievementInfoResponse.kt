package com.citysavior.android.data.dto.achievement.response

import com.citysavior.android.domain.model.achievement.Achievement
import com.citysavior.android.domain.model.report.Category

data class AchievementInfoResponse(
    val achievements : List<AchievementDto>,
)

data class AchievementDto(
    val name : String,
    val description : String,
    val iconUrl : String,
    val category : Category,
    val goalCount : Int,
)


fun AchievementDto.toDomain() = Achievement(
    name = name,
    description = description,
    iconUrl = iconUrl,
    category = category,
    goalCount = goalCount,
)