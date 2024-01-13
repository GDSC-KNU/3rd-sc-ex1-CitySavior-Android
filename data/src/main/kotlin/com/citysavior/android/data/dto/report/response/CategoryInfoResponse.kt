package com.citysavior.android.data.dto.report.response

data class CategoryInfoResponse(
    val categories : List<CategoryDto>,
)

data class CategoryDto(
    val id : Long,
    val name : String,
    val description : String,
)
