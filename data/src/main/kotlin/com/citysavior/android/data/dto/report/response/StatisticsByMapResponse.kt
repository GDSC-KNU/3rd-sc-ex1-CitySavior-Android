package com.citysavior.android.data.dto.report.response

import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.StatisticsDetail

data class StatisticsByMapResponse(
    val totalReports : Int,
    val resolvedReports : Int,
    val statisticsDetails : List<StatisticsDetailDto>,
)

data class StatisticsDetailDto(
    val category : Category,
    val totalReports : Int,
    val resolvedReports : Int,
)


fun StatisticsDetailDto.toDomain() = StatisticsDetail(
    category = category,
    totalReports = totalReports,
    resolvedReports = resolvedReports,
)