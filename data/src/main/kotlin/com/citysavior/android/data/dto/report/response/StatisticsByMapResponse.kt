package com.citysavior.android.data.dto.report.response

import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.ReportStatistics
import com.citysavior.android.domain.model.report.StatisticsDetail

data class StatisticsByMapResponse(
    val totalReports : Int,
    val resolvedReports : Int,
    val statisticsDetails : Map<Category,StatisticsDetailDto>,
)

data class StatisticsDetailDto(
    val totalReports : Int,
    val resolvedReports : Int,
)

fun StatisticsByMapResponse.toDomain() = ReportStatistics(
    totalReports = totalReports,
    resolvedReports = resolvedReports,
    statisticsDetails = statisticsDetails.keys.map {
        statisticsDetails[it]!!.toDomain(it)
    }
)


fun StatisticsDetailDto.toDomain(category: Category) = StatisticsDetail(
    category = category,
    totalReports = totalReports,
    resolvedReports = resolvedReports,
)