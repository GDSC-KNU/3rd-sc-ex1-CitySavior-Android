package com.citysavior.android.data.dto.user.response

import com.citysavior.android.domain.model.achievement.Achievement
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.user.AchieveProgress

data class UserInfoResponse(
    val totalReportCount : Int,
    val totalRepairedCount : Int,
    val achieveCollectCount : Int,
    val achieveProgressing : List<AchieveProgressDto>
)

data class AchieveProgressDto(
    val category: Category,
    val iconUrl : String,
    val name : String,
    val description : String,
    val progressCount : Int,
    val goalCount : Int,
)

fun AchieveProgressDto.toDomain() = AchieveProgress(
    achievement = Achievement(
        category = category,
        iconUrl = iconUrl,
        name = name,
        description = description,
        goalCount = goalCount,
    ),
    progressCount = progressCount,
)