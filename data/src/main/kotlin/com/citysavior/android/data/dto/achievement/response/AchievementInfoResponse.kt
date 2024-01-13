package com.citysavior.android.data.dto.achievement.response

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
