package com.citysavior.android.data.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.citysavior.android.domain.model.report.Category

@Entity(tableName = "category")
class CategoryEntity (
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    val name : String,
    val description : String,
)

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    description = description,
)
