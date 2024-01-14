package com.citysavior.android.data.dto.report.response

import com.citysavior.android.data.entity.category.CategoryEntity
import com.citysavior.android.domain.model.report.Category

data class CategoryInfoResponse(
    val categories : List<CategoryDto>,
)

data class CategoryDto(
    val id : Long,
    val name : String,
    val description : String,
)

fun CategoryDto.toEntity() = CategoryEntity(
    id = id,
    name = name,
    description = description,
)
fun CategoryDto.toDomain() = Category(
    id = id,
    name = name,
    description = description,
)
