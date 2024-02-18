package com.citysavior.android.data.dto.report.request

import com.citysavior.android.domain.model.report.Category

data class CreateReportRequestPart(
    val latitude: Double,
    val longitude: Double,
    val description : String,
    val category : Category,
)
