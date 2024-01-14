package com.citysavior.android.domain.model.user

import com.citysavior.android.domain.model.achievement.Achievement

class UserInfo(
    val totalReportCount : Int,
    val totalRepairedCount : Int,
    val achieveCollectCount : Int,
    val achieveProgressingList : List<AchieveProgress>,
){
    companion object{
        fun fixture(
            totalReportCount : Int = 1,
            totalRepairedCount : Int = 1,
            achieveCollectCount : Int = 0,
            achieveProgressingList : List<AchieveProgress> = listOf(AchieveProgress.fixture()),
        ) : UserInfo{
            return UserInfo(
                totalReportCount = totalReportCount,
                totalRepairedCount = totalRepairedCount,
                achieveCollectCount = achieveCollectCount,
                achieveProgressingList = achieveProgressingList,
            )
        }
    }
}

class AchieveProgress(
    val achievement: Achievement,
    val progressCount : Int,
){
    companion object{
        fun fixture(
            achievement: Achievement = Achievement.fixture(),
            progressCount : Int = 1,
        ) : AchieveProgress{
            return AchieveProgress(
                achievement = achievement,
                progressCount = progressCount,
            )
        }
    }
}