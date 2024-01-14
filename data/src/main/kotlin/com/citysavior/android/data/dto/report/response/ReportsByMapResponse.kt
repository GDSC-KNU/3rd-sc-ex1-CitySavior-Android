package com.citysavior.android.data.dto.report.response

import com.citysavior.android.data.entity.category.CategoryEntity
import com.citysavior.android.data.entity.category.toDomain
import com.citysavior.android.domain.model.report.ReportPoint

data class ReportsByMapResponse(
    val points: List<ReportPointDto>,
)

data class ReportPointDto(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val categoryId : Long,
    val weight : Int,
)


fun ReportPointDto.toDomain(
    categoryEntity: CategoryEntity,
) = ReportPoint(
    id = id,
    latitude = latitude,
    longitude = longitude,
    category = categoryEntity.toDomain(),
    weight = weight,
)