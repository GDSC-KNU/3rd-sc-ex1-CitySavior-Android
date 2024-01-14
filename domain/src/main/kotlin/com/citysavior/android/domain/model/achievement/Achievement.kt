package com.citysavior.android.domain.model.achievement

import com.citysavior.android.domain.model.report.Category

class Achievement(
    val name : String,
    val description : String,
    val iconUrl : String,
    val category : Category,
    val goalCount : Int,
){
    companion object{
        fun fixture(
            name : String = "도시의 지킴이",
            description : String = "공공시설 신고 3건 달성하기",
            iconUrl : String = "https://picsum.photos/200/200",
            category : Category = Category.fixture(),
            goalCount : Int = 3,
        ) : Achievement{
            return Achievement(
                name = name,
                description = description,
                iconUrl = iconUrl,
                category = category,
                goalCount = goalCount,
            )
        }
    }
}