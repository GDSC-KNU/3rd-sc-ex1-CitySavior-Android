package com.citysavior.android.data.dto.achievement.response

import com.citysavior.android.data.dto.report.response.CategoryDto
import com.citysavior.android.data.dto.report.response.toDomain
import com.citysavior.android.data.entity.category.CategoryEntity
import com.citysavior.android.data.entity.category.toDomain
import com.citysavior.android.domain.model.achievement.Achievement

data class AchievementInfoResponse(
    val achievements : List<AchievementDto>,
)

data class AchievementDto(
    val name : String,
    val description : String,
    val iconUrl : String,
    val categoryId : Long,
    val goalCount : Int,
)


fun AchievementDto.toDomain(
    categoryEntity: CategoryEntity,
) = Achievement(
    name = name,
    description = description,
    iconUrl = iconUrl,
    category = categoryEntity.toDomain(),
    goalCount = goalCount,
)