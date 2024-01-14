package com.citysavior.android.data.dto.report.response

import com.citysavior.android.data.entity.category.CategoryEntity
import com.citysavior.android.data.entity.category.toDomain
import com.citysavior.android.domain.model.report.ReportStatistics
import com.citysavior.android.domain.model.report.StatisticsDetail

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


fun StatisticsDetailDto.toDomain(
    categoryEntity: CategoryEntity,
) = StatisticsDetail(
    category = categoryEntity.toDomain(),
    totalReports = totalReports,
    resolvedReports = resolvedReports,
)