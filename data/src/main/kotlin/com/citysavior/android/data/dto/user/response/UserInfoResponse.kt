package com.citysavior.android.data.dto.user.response

import com.citysavior.android.data.entity.category.CategoryEntity
import com.citysavior.android.data.entity.category.toDomain
import com.citysavior.android.domain.model.achievement.Achievement
import com.citysavior.android.domain.model.user.AchieveProgress

data class UserInfoResponse(
    val totalReportCount : Int,
    val totalRepairedCount : Int,
    val achieveCollectCount : Int,
    val achieveProgressing : List<AchieveProgressDto>
)

data class AchieveProgressDto(
    val categoryId: Long,
    val iconUrl : String,
    val name : String,
    val description : String,
    val progressCount : Int,
    val goalCount : Int,
)

fun AchieveProgressDto.toDomain(
    categoryEntity : CategoryEntity,
) = AchieveProgress(
    achievement = Achievement(
        category = categoryEntity.toDomain(),
        iconUrl = iconUrl,
        name = name,
        description = description,
        goalCount = goalCount,
    ),
    progressCount = progressCount,
)