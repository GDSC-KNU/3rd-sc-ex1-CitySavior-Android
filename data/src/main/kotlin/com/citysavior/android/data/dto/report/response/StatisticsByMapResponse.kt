package com.citysavior.android.data.dto.report.response

data class StatisticsByMapResponse(
    val totalReports : Int,
    val resolvedReports : Int,
    val statisticsDetails : List<StatisticsDetailDto>,
)

data class StatisticsDetailDto(
    val categoryId : Long,
    val totalReports : Int,
    val resolvedReports : Int,
)
