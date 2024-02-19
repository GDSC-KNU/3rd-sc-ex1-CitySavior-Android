package com.citysavior.android.domain.params.report

import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.Point
import java.io.File

data class CreateReportParams(
    val file: File,
    val point: Point,
    val description : String,
    val category : Category,
)
