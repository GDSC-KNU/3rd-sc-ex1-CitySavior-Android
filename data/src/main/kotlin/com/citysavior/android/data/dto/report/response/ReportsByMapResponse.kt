package com.citysavior.android.data.dto.report.response

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
