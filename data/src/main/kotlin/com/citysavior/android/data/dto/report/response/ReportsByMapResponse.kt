package com.citysavior.android.data.dto.report.response

import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPoint

data class ReportsByMapResponse(
    val points: List<ReportPointDto>,
)

data class ReportPointDto(
    val reportId: Long,
    val latitude: Double,
    val longitude: Double,
    val category : Category,
    val weight : Double,
)


fun ReportPointDto.toDomain() = ReportPoint(
    id = reportId,
    point = Point(
        latitude = latitude,
        longitude = longitude,
    ),
    category = category,
    weight = weight.toInt(),
)

fun List<ReportPointDto>.toDomain() = map(ReportPointDto::toDomain)