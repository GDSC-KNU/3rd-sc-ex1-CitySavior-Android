package com.citysavior.android.presentation.main.map

import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.presentation.R

fun Category.getPaintRes(): Int{
    return when(this){
        Category.ROAD_TRAFFIC -> R.drawable.traffic_icon_30
        Category.STREET -> R.drawable.street_icon_30
        Category.BUILD_STRUCTURE -> R.drawable.build_icon_50
        Category.ENVIRONMENT -> R.drawable.environment_icon_32
        Category.PARK_PUBLIC -> R.drawable.park_icon30
        Category.CITY_OBSTACLE -> R.drawable.obstacle_icon_48
        Category.SCHOOL_ZONE -> R.drawable.scholl_icon_30
        Category.OTHER -> R.drawable.other_icon_30
    }
}