package com.citysavior.android.data.dto.report.request

import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.params.report.CreateReportParams

data class CreateReportRequestPart(
    val latitude: Double,
    val longitude: Double,
    val description : String,
    val category : Category,
)

fun CreateReportParams.toData() = CreateReportRequestPart(
    latitude = point.latitude,
    longitude = point.longitude,
    description = description,
    category = category,
)