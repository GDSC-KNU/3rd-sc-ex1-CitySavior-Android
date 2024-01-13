package com.citysavior.android.data.dto.user.response

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