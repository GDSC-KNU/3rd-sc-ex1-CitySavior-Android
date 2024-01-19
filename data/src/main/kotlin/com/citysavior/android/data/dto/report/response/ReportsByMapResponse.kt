package com.citysavior.android.data.dto.report.response

import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.ReportPoint

data class ReportsByMapResponse(
    val points: List<ReportPointDto>,
)

data class ReportPointDto(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val category : Category,
    val weight : Int,
)


fun ReportPointDto.toDomain() = ReportPoint(
    id = id,
    latitude = latitude,
    longitude = longitude,
    category = category,
    weight = weight,
)